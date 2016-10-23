package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.AdapterList.ContactItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Kontak;
import com.letshoppa.feechan.letshoppa.Class.KontakLoadTask;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.EditKontakActivity;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KontakFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KontakFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KontakFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KontakFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KontakFragment newInstance(String param1, String param2) {
        KontakFragment fragment = new KontakFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView emailTextView;
    EditText contactTypeEditText;
    EditText contactEditText;
    Button addContactButton;

    ArrayAdapter mAdapter;
    private List listKontaks;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kontak, container, false);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        //set email primary
        if(AppHelper.currentAccount != null) {
            emailTextView.setText(AppHelper.currentAccount.getEmail());
        }
        contactTypeEditText = (EditText) view.findViewById(R.id.contactTypeEditText);
        contactEditText = (EditText) view.findViewById(R.id.contactEditText);
        addContactButton = (Button) view.findViewById(R.id.add_contact_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactDialog();
            }
        });

        //set listview
        listView = (ListView) view.findViewById(R.id.MyContactListView);
        listKontaks = new ArrayList();
        mAdapter = new ContactItemAdapter(getActivity(),listKontaks);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Kontak item = (Kontak)parent.getItemAtPosition(position);
                detailKontak(item);
                return true;
            }
        });
        refreshKontak();
        return view;
    }

    private void refreshKontak()
    {
        if(AppHelper.currentAccount != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllKontakByAccountId.php";
            int accountid = Integer.valueOf(AppHelper.currentAccount.getAccountid());
            KontakLoadTask task = new KontakLoadTask(url, accountid, mAdapter, getActivity());
            task.execute();
        }
    }

    private void detailKontak(final Kontak kontak)
    {
        final CharSequence[] items = { getString(R.string.edit), getString(R.string.delete),getString(R.string.cancel) };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                switch (item)
                {
                    case 0:
                        editKontak(kontak);
                        break;
                    case 1:
                        deleteKontak(kontak);
                        break;
                    default:

                }
            }

        });

        android.app.AlertDialog alert = builder.create();

        alert.show();
    }

    private void addContactDialog() {
        boolean cancel = false;
        String contactType = contactTypeEditText.getText().toString();
        String contact = contactEditText.getText().toString();

        View focusView = null;
        if (TextUtils.isEmpty(contact))
        {
            focusView = contactEditText;
            contactEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(TextUtils.isEmpty(contactType))
        {
            focusView = contactEditText;
            contactTypeEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(!cancel) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            addContact();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.add_contact) + "?")
                    .setPositiveButton(getString(R.string.yes), dialogClickListener).
                    setNegativeButton(getString(R.string.no), dialogClickListener).show();
        }
        else
        {
            focusView.requestFocus();
        }
    }
    private void addContact()
    {
        //add contact
        if(AppHelper.currentAccount != null) {
            String jeniskontak = contactTypeEditText.getText().toString();
            String isikontak = contactEditText.getText().toString();
            int accountid = Integer.valueOf(AppHelper.currentAccount.getAccountid());
            String url = AppHelper.domainURL +"/AndroidConnect/AddKontak.php";

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Kontak.TAG_ACCOUNTID, String.valueOf(accountid)));
            parameter.add(new BasicNameValuePair(Kontak.TAG_ISIKONTAK, isikontak));
            parameter.add(new BasicNameValuePair(Kontak.TAG_JENISKONTAK, jeniskontak));
            UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity(),new RefreshMethod());
            task.execute();
        }
    }

    private void editKontak(Kontak kontak)
    {
        Intent editKontakAct = new Intent(getActivity(),EditKontakActivity.class);
        editKontakAct.putExtra(Kontak.TAG_KONTAK,kontak);
        startActivity(editKontakAct);
    }

    private void deleteKontak(final Kontak kontak)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status
                        String url= AppHelper.domainURL + "/AndroidConnect/DeleteKontak.php";
                        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                        parameter.add(new BasicNameValuePair(Kontak.TAG_KONTAKID,String.valueOf(kontak.getKontakid())));
                        UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity(),new RefreshMethod());
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.prompt_delete_contact)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }

    public class RefreshMethod implements IRefreshMethod
    {
        @Override
        public void refresh() {
            refreshKontak();
            contactEditText.setText("");
            contactTypeEditText.setText("");
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshKontak();
    }
}
