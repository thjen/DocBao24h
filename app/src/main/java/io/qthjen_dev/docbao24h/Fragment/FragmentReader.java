package io.qthjen_dev.docbao24h.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.InterstitialAd;
import com.sdsmdg.tastytoast.TastyToast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.qthjen_dev.docbao24h.Activity.BaoNCCActivity;
import io.qthjen_dev.docbao24h.Activity.InterstitialAdsActivity;
import io.qthjen_dev.docbao24h.Adapter.AdapterReader;
import io.qthjen_dev.docbao24h.Model.MyReader;
import io.qthjen_dev.docbao24h.Model.TabInfo;
import io.qthjen_dev.docbao24h.R;
import io.qthjen_dev.docbao24h.Utils.CopyLinkListener;
import io.qthjen_dev.docbao24h.Utils.FinalUtils;
import io.qthjen_dev.docbao24h.Utils.ItemAdapterListener;
import io.qthjen_dev.docbao24h.Utils.NetworkUtils.NetworkTools;
import io.qthjen_dev.docbao24h.Utils.SetAndGetPage;
import io.qthjen_dev.docbao24h.Utils.SignInAndShareFacebook;
import io.qthjen_dev.docbao24h.Utils.SignInAndShareGooglePlus;
import io.qthjen_dev.docbao24h.Utils.SignInAndShareTwitter;
import io.qthjen_dev.docbao24h.Utils.XMLDOMParse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentReader extends Fragment implements ItemAdapterListener,
        SignInAndShareFacebook, SignInAndShareGooglePlus,
        SignInAndShareTwitter, CopyLinkListener {

    private List<TabInfo> list;
    private int position; // position get from constructor;
    private View myView;
    private RecyclerView mRecycler;

    private AdapterReader mAdapter;
    private List<MyReader> mList;

    private String title = "", link = "", image = "", date = "";

    private SearchView mSearchView;
    private CustomTabsClient mClient;

    private CallbackManager mCallbackManager;
    /** in dialog sign in facebook **/
    private LoginButton mFacebookLoginButton;
    private ShareDialog mShareDialog;
    private ShareLinkContent mShare;
    private ImageView mIvBack;
    private Button mButtonLogoutFb;
    private TextView mShowFbShareDialog;

    private InterstitialAd mInterstitialAd;

    public FragmentReader() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FragmentReader(int position, List<TabInfo> list) {
        this.list = list;
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        myView = inflater.inflate(R.layout.fragment_fragment_reader, container, false);
        mCallbackManager = CallbackManager.Factory.create();

        mRecycler = myView.findViewById(R.id.recyclerReader);

        /*try {
            PackageInfo info = null;
            try {
                info = getContext().getPackageManager().getPackageInfo(
                        "io.qthjen_dev.docbao24h",
                        PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {}*/

        //mTvDescription.setText("");
        title = "";
        date = "";
        link = "";
        image = "";

        mList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);

        /** check internet true (setup adapter) false (show toast from main activity) **/
        if (NetworkTools.CheckNetwork(getContext())) {
            mList.clear();
            new GetUrl().execute(list.get(position).getLink());
            mAdapter = new AdapterReader(mList, getContext(), this, this, this, this, this);
        }
        mRecycler.setAdapter(mAdapter);

        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_main, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.mn_search).getActionView();

        /** settext color for search **/
        EditText txtSearch = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(Color.WHITE);

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(((AppCompatActivity) getContext()).getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.mn_search) {
            return true;
        } else if (item.getItemId() == R.id.mn_chooseBao) {
            Intent intent = new Intent(getActivity(), BaoNCCActivity.class);
            startActivityForResult(intent, FinalUtils.RESULT_BAO_NCC);
            getActivity().overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay_x);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MyReader item) {
        openChromeCustomTab(item.getLink().trim());
        warmUpChrome();
    }

    /** setup chrome custom tab **/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openChromeCustomTab(String link) {
        Uri uri = Uri.parse(link);
        if (uri == null) {
            return;
        }

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setData(uri);
        customTabsIntent.intent.putExtra(FinalUtils.EXTRA_CUSTOM_TABS_TOOLBAR_COLOR, getResources().getColor(R.color.colorPrimary));

        PackageManager packageManager = getContext().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(customTabsIntent.intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (TextUtils.equals(packageName, FinalUtils.PACKAGE_NAME))
                customTabsIntent.intent.setPackage(FinalUtils.PACKAGE_NAME);
        }

        customTabsIntent.launchUrl(getContext(), uri);
    }

    private void warmUpChrome() {
        CustomTabsServiceConnection service = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
                mClient.warmup(0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(getContext(),FinalUtils.PACKAGE_NAME, service);
    }

    @Override
    public void onSignInAndShare(final MyReader item) {
        final Dialog dialog1 = new Dialog(getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialog_signin_facebook);
        mFacebookLoginButton = dialog1.findViewById(R.id.login_button);
        mIvBack = dialog1.findViewById(R.id.iv_backfacebooksignin);
        mShowFbShareDialog = dialog1.findViewById(R.id.tv_showFbShareDialog);
        mButtonLogoutFb = dialog1.findViewById(R.id.bt_logoutfb);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });

        mFacebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        /** login facebook **/
        mFacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //mAvatar.setVisibility(View.VISIBLE);
                        //mTvName.setVisibility(View.VISIBLE);
                        //mTvName.setVisibility(View.VISIBLE);
                        //resultFbData();
                        //dialog1.cancel();
                        mShowFbShareDialog.setVisibility(View.VISIBLE);
                        mFacebookLoginButton.setVisibility(View.GONE);
                        mButtonLogoutFb.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
            }
        });

        mButtonLogoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                mFacebookLoginButton.setVisibility(View.VISIBLE);
                mButtonLogoutFb.setVisibility(View.GONE);
                mShowFbShareDialog.setVisibility(View.GONE);
            }
        });

        if (checkSignInFacebook()) {
            mShowFbShareDialog.setVisibility(View.VISIBLE);
            mButtonLogoutFb.setVisibility(View.VISIBLE);
            mFacebookLoginButton.setVisibility(View.GONE);
        }

        /** share link with facebook sdk **/
        mShowFbShareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareDialog = new ShareDialog(FragmentReader.this);
                String data = item.getLink() + "";
                String title = item.getTitle();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    mShare = new ShareLinkContent.Builder()
                            .setContentTitle(title)
                            .setContentUrl(Uri.parse(data)).build();
                }
                mShareDialog.show(mShare);
            }
        });

        dialog1.show();
    }

    /*private void resultFbData() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Picasso.with(ReaderActivity.this).load(object.getString("id")).placeholder(R.drawable.user).into(mAvatar);
                    mTvName.append(object.getString("name"));
                    mTvName.setText(object.getString("first_name"));
                    mTvEmail.setText(object.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameter = new Bundle();
        parameter.putString("fields", "name,email,first_name");
        graphRequest.setParameters(parameter);
        graphRequest.executeAsync();
    }*/

    private boolean checkSignInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == FinalUtils.RESULT_BAO_NCC && resultCode == RESULT_OK && data != null) {
            /** refresh activity when have change date from baonccactivity
             * because we need update data from shared preference **/
            boolean nccChanged = data.getBooleanExtra("trangbao", false);
            if ( nccChanged) {
                //mListTab.clear();
                Intent intent1 = (getActivity()).getIntent();
                (getActivity()).finish();
                (getActivity()).overridePendingTransition(0,0);
                startActivity(intent1);
                (getActivity()).overridePendingTransition(0,0);
                mAdapter.notifyDataSetChanged();
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSignInAndShareGoogleP(MyReader item) {
        TastyToast.makeText(getContext(), getResources().getString(R.string.functionHasNotBeenDevelop), TastyToast.LENGTH_LONG, TastyToast.INFO);
    }

    @Override
    public void onSignInAndShareTwitter(MyReader item) {
        TastyToast.makeText(getContext(), getResources().getString(R.string.functionHasNotBeenDevelop), TastyToast.LENGTH_LONG, TastyToast.INFO);
    }

    @Override
    public void onCopyLinkListener(MyReader item) {
        copyClipBoard(getContext(), item.getLink());
        TastyToast.makeText(getContext(), getResources().getString(R.string.linkHasBeenSaveClipboard), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    private void copyClipBoard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        } else {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clipData = android.content.ClipData.newPlainText("Copied Text", text);
            clipboardManager.setPrimaryClip(clipData);
        }
    }

    /** get all xml from link **/
    class GetUrl extends AsyncTask<String, String, String> {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        @Override
        protected String doInBackground(String... strings) {
            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]);

            Request request = builder.build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            /** parsing xml **/
            //if (!s.equals("")) {

            /** to do something **/
            //mTvDescription.append(s + "");
            try {
                XMLDOMParse parse = new XMLDOMParse();
                Document document = parse.getDocument(s);
                NodeList nodeList = document.getElementsByTagName("item");
                NodeList nodeListDescription = document.getElementsByTagName("description");
                //NodeList nodeListContent = document.getElementsByTagName("content:encoded");
                NodeList nodeListTitle = document.getElementsByTagName("title");
                NodeList nodeListLink = document.getElementsByTagName("link");
                NodeList nodeListDate = document.getElementsByTagName("pubDate");

                for (int i = 1; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    String cdDes = nodeListDescription.item(i+1).getTextContent();
                    //Pattern pattern = Pattern.compile("<src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                    Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                    Matcher matcher = pattern.matcher(cdDes);
                    if (matcher.find())
                        image = matcher.group(1);
                    if (nodeListTitle.item(i + 1).getTextContent() != null)
                        title = nodeListTitle.item(i + 1).getTextContent();
                    else
                        title = parse.getValue(element, "title");
                    if (nodeListLink.item(i + 1).getTextContent() != null)
                        link = nodeListLink.item(i + 1).getTextContent();
                    else
                        link = parse.getValue(element, "link");
                    if (nodeListDate.item(i + 1).getTextContent() != null)
                        date = nodeListDate.item(i + 1).getTextContent();
                    else
                        date = parse.getValue(element, "pubDate");

                    mList.add(new MyReader(title, date, image, link));
                    mAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                //Log.d("firstmydebugg", s + "");
            }

            //} else {}

            super.onPostExecute(s);
        }
    }
}