package com.developer.manuelquinteros.clinicadentalx.Contact.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.developer.manuelquinteros.clinicadentalx.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterFragment extends Fragment {
    WebView webView;

    public TwitterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_twitter, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        webView = (WebView)view.findViewById(R.id.webTwitter);
        webView.loadUrl("https://twitter.com/ManuelQH10");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //elimina ProgressBar.
                progressDialog.dismiss();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
