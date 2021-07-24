package sharefiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.projectclassroom.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class DocumentViewer extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    ImageView imageView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_viewer);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();
        String finalurl=null;

        progressBar=findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);
        imageView=findViewById(R.id.imgfile);
        String imageurl=i.getStringExtra("myimageurl");
        String url = i.getStringExtra("myurl");
        if (imageurl!=null){
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(imageurl).into(imageView);
        }
        if(url!=null){
            webView = findViewById(R.id.webview);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setAppCacheEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setSaveFormData(true);
            webSettings.setEnableSmoothTransition(true);
            try {
                finalurl = "https://drive.google.com/viewerng/viewer?embeded=true&url=" + URLEncoder.encode(url, "UTF-8");
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if (newProgress == 100) {
                            progressBar.setVisibility(View.GONE);
                            webView.loadUrl(url);
                        }
                    }
                });
                webView.loadUrl(finalurl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}