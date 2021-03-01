package com.flitzen.cng.fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.cng.R;
import com.flitzen.cng.adapter.CreditNoteListAdapter;
import com.flitzen.cng.model.CrediNotesListModel;
import com.flitzen.cng.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditNotesFragment extends Fragment {

    View viewCreditsNotes;

    @BindView(R.id.view_creditnote_content)
    LinearLayout viewInvoice;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_clear_search)
    ImageView img_clear_search;
    @BindView(R.id.swipe_view_creditnote_list)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview_creditnote_list)
    RecyclerView recyclerview_crnote_list;
    @BindView(R.id.layout_empty)
    RelativeLayout layout_empty;
    @BindView(R.id.textViewMsg)
    TextView textViewMsg;

    CreditNoteListAdapter creditNoteListAdapter;
    ArrayList<CrediNotesListModel.Result> arrayList=new ArrayList<>();
    ArrayList<CrediNotesListModel.Result> arrayListTemp=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCreditsNotes = inflater.inflate(R.layout.fragment_credit_notes, container, false);
        ButterKnife.bind(this,viewCreditsNotes);
        performSomeOperation();
        return viewCreditsNotes;
    }

    private void performSomeOperation() {
        recyclerview_crnote_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        creditNoteListAdapter = new CreditNoteListAdapter(getActivity(), arrayListTemp);
        recyclerview_crnote_list.setAdapter(creditNoteListAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int ii, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    arrayListTemp.clear();

                    img_clear_search.setVisibility(View.VISIBLE);
                    img_search.setVisibility(View.GONE);

                    String word = charSequence.toString().toLowerCase().trim();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getCreditUniqueId().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayList.get(i));
                        } else if (arrayList.get(i).getSalesPerson().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayList.get(i));
                        } else if (arrayList.get(i).getCreditTo().toLowerCase().contains(word)) {
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                } else {
                    img_clear_search.setVisibility(View.GONE);
                    img_search.setVisibility(View.VISIBLE);

                    arrayListTemp.addAll(arrayList);
                }
                creditNoteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        img_clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.playClickSound(getActivity());
                edtSearch.setText("");

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                arrayList.clear();
                arrayList.addAll(arrayListTemp);
                creditNoteListAdapter.notifyDataSetChanged();

                img_clear_search.setVisibility(View.GONE);
            }
        });

    }
}
