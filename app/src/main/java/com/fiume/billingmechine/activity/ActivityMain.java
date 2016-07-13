package com.fiume.billingmechine.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;
import com.cie.btp.PrinterWidth;
import com.fiume.billingmechine.R;
import com.fiume.billingmechine.adapter.BillListAdapter;
import com.fiume.billingmechine.adapter.MainGridAdapter;
import com.fiume.billingmechine.db.DatabaseHelper;
import com.fiume.billingmechine.model.BillModel;
import com.fiume.billingmechine.utils.SimpleGestureFilter;
import com.fiume.billingmechine.utils.SimpleGestureFilter.SimpleGestureListener;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Razi on 12/15/2015.
 */
public class ActivityMain extends AppCompatActivity implements BillListAdapter.OnDeleteListener, SimpleGestureListener {

    private static final String title_connecting = "connecting...";
    private static final String title_connected_to = "connected: ";
    private static final String title_not_connected = "not connected";
    private static final CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;
    private static List<BillModel> billList;
    private final Messenger mMessenger = new Messenger(new PrintSrvMsgHandler());
    float totalPrice, totalQty;
    private Button btn050, btn1, btn150, btn2, btn3, btn5, btn10, btnPrint, btnSave;
    private ImageButton btnClear;
    private GridView gvMain;
    private ListView lvBill;
    private MainGridAdapter gridAdapter;
    private BillListAdapter billAdapter;
    private DatabaseHelper dbHelper;
    private String id, name, price, qty, arName, billNo, totalAmnt, itemTotal;
    private String lastId;
    private boolean addQty, editItem;
    private int editItemId;
    private TextView tvTotalPrice, tvTotalQty;
    private WebView webView;
    private Bitmap bm;
    private String html, htmlContent, htmlHead, htmlFooter;
    private double nqty, nprice;
    private SimpleGestureFilter detector;
    private SimpleDateFormat dateFormatter;
    private String mConnectedDeviceName = "";
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


        /////// Bluetooth printer connection   //////////////////


        dbHelper = new DatabaseHelper(this);
        gridAdapter = new MainGridAdapter(getApplicationContext(), dbHelper.getItemsData());
        billList = new ArrayList<>();// new ArrayList<BillModel>();

        dateFormatter = new SimpleDateFormat("MMM dd,yyyy  hh:mm:ss a",
                Locale.US);


        detector = new SimpleGestureFilter(this, this);

        billAdapter = new BillListAdapter(this, billList, false);
        editItem = false;

        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvTotalQty = (TextView) findViewById(R.id.tv_total_qty);

        iv = (ImageView) findViewById(R.id.iv_html);


        btn050 = (Button) findViewById(R.id.btn_050);
        btn1 = (Button) findViewById(R.id.btn_1);
        btn150 = (Button) findViewById(R.id.btn_150);
        btn2 = (Button) findViewById(R.id.btn_2);
        btn3 = (Button) findViewById(R.id.btn_3);
        btn5 = (Button) findViewById(R.id.btn_5);
        btn10 = (Button) findViewById(R.id.btn_10);
        btnPrint = (Button) findViewById(R.id.btn_print);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnClear = (ImageButton) findViewById(R.id.btn_clear);
        gvMain = (GridView) findViewById(R.id.grid_main);
        gvMain.setAdapter(gridAdapter);
        lvBill = (ListView) findViewById(R.id.lv_bill);
        lvBill.setAdapter(billAdapter);

        qty = "";
        addQty = false;

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billList.clear();
                billAdapter.notifyDataSetChanged();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();
                String time = dateFormatter.format(newCalendar.getTime());//.toString();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String filterDate = df.format(newCalendar.getTime());

                if (billList.size() > 0) {
                    dbHelper.insertBillHeaderData(time, tvTotalPrice.getText().toString(), tvTotalQty.getText().toString(), filterDate);
                    for (int i = 0; i < billList.size(); i++) {
                        BillModel model = billList.get(i);
                        dbHelper.insertBillDetailData(String.valueOf(dbHelper.getLastBillId()), model.getId(), model.getName(),
                                model.getArName(), model.getUnitPrice(), model.getPrice(), model.qty, filterDate);
                    }

                    Toast.makeText(getApplicationContext(), "Bill Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No Bill To Save", Toast.LENGTH_SHORT).show();
                }

                billList.clear();
                billAdapter.notifyDataSetChanged();
                tvTotalPrice.setText("");
                tvTotalQty.setText("");

            }
        });

        btn050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0050", "half rial", "نصف الريال", "0.50", "1", "0.50");
                totalCalc();
                //lastId = "0050";
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0001", "one rial", "ريال", "1.00", "1", "1.00");
                totalCalc();
            }
        });
        btn150.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0150", "one and half rial", "واحد ونصف الريال", "1.50", "1", "1.50");
                totalCalc();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0002", "two rial", "اثنين من الريال", "2.00", "1", "2.00");
                totalCalc();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0003", "three rial", "ثلاثة الريال", "3.00", "1", "3.00");
                totalCalc();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0005", "five rial", "خمسة الريال", "5.00", "1", "5.00");
                totalCalc();
            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBillItemModel("0010", "ten rial", "عشرة ريال", "10.00", "1", "10.00");
                totalCalc();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar newCalendar = Calendar.getInstance();
                String time = dateFormatter.format(newCalendar.getTime());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String filterDate = df.format(newCalendar.getTime());

                if (billList.size() > 0) {
                    dbHelper.insertBillHeaderData(time, tvTotalPrice.getText().toString(), tvTotalQty.getText().toString(), filterDate);

                    StringBuilder sb = new StringBuilder("");

                    for (int i = 0; i < billList.size(); i++) {
                        BillModel model = billList.get(i);
                        htmlContent = "<tr>\n" +
                                "              <td><strong>" + model.getPrice() + "</strong></td>\n" +
                                "              <td><strong>" + model.getUnitPrice() + "</strong></td>\n" +
                                "              <td><strong>" + model.getQty() + "</strong></td>\n" +
                                "              <td><strong>" + model.getArName() + "</strong></td>\n" +
                                "            </tr>\n";
                        sb.append(htmlContent);
                        dbHelper.insertBillDetailData(String.valueOf(dbHelper.getLastBillId()), model.getId(), model.getName(),
                                model.getArName(), model.getUnitPrice(), model.getPrice(), model.qty, filterDate);
                    }

                    billNo = String.valueOf(dbHelper.getLastBillId());
                    if (billNo.length() == 1)
                        billNo = "00" + String.valueOf(dbHelper.getLastBillId());
                    else if (billNo.length() == 2)
                        billNo = "0" + String.valueOf(dbHelper.getLastBillId());

                    htmlHead = "<head>\n" +
                            "    <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" +
                            "  </head>\n" +
                            "  <style>\n" +
                            "      .bill{\n" +
                            "        width: 565px;\n" +
                            "        margin: auto;\n" +
                            "        font-family: 'arial';\n" +
                            "      }\n" +
                            "      table {\n" +
                            "      width : 100%;\n" +
                            "      }\n" +
                            "      \n" +
                            "      table, th, tfoot {\n" +
                            "        border-collapse: collapse;      \n" +
                            "      }\n" +
                            "      p{\n" +
                            "        text-align: center;\n" +
                            "      }\n" +
                            "      thead tr, tfoot tr{\n" +
                            "        border-bottom: 3px dotted #000;\n" +
                            "        border-top: 3px dotted #000;        \n" +
                            "      }\n" +
                            "      thead tr th, tfoot tr td, tbody tr td{        \n" +
                            "        text-align: right;\n" +
                            "        padding: 5px;\n" +
                            "      }\n" +
                            "      thead tr th, tfoot tr td{\n" +
                            "        font-weight: bold;\n" +
                            "      }\n" +
                            "      thead tr th, tfoot tr td, tbody tr td{\n" +
                            "        font-size: 24px;\n" +
                            "\n" +
                            "      }\n" +
                            "\n" +
                            "      tbody tr td:last-child, tfoot tr td:last-child, p strong{\n" +
                            "        font-weight: bold;\n" +
                            "        font-size: 24px;\n" +
                            "      }\n" +
                            "tbody tr td:last-child{\n" +
                            " font-size: 24px;\n" +
                            "}\n" +
                            "      tbody tr:first-child td {\n" +
                            "        padding-top: 15px;\n" +
                            "      }\n" +
                            "      tbody tr:last-child td {\n" +
                            "        padding-bottom: 15px;\n" +
                            "      }\n" +
                            "\n" +
                            "      </style>\n" +
                            "\n" +
                            "\t\n" +
                            "\n" +
                            "      <div class=\"bill\">\n" +
                            "        <h3 style=\"text-align:center\"> <strong>Musallal-eid, Sabya </strong> <br/>\n" +
                            "        <small style=\"font-size: 22px;\">Tel:0509846404, Email:broast51@gmail.com</small></h3>\n" +
                            "        <p style=\"text-align: left\"> <strong>Bill No: " + billNo + "</strong></p>\n" +
                            "        <table>\n" +
                            "          <thead>\n" +
                            "            <tr>\n" +
                            "              <th colspan=\"1\" width=\"20%\">Total</th>\n" +
                            "              <th width=\"20%\">Price</th>        \n" +
                            "              <th width=\"10%\">Qty</th>\n" +
                            "              <th>Item</th>\n" +
                            "            </tr>\n" +
                            "          </thead>\n" +
                            "          \n" +
                            "          <tbody>";


                    htmlFooter = "          </tbody>\n" +
                            "          \n" +
                            "        <tfoot>\n" +
                            "          <tr>\n" +
                            "                    <td>" + tvTotalPrice.getText().toString() + "</td>\n" +
                            "                    <td> </td>        \n" +
                            "                    <td>" + tvTotalQty.getText().toString() + "</td>\n" +
                            "                    <td>الإجمالي</td>\n" +
                            "                  </tr>\n" +
                            "                          </tfoot>\n" +
                            "                  </table>\n" +
                            "                  <p style=\"font-size:24px\"><strong>" + tvTotalPrice.getText().toString() + "  المجموع الإجمالي</strong></p>\n" +
                            "                  <p style=\"font-size:22px\">&curren; شكرا لك، و زيارة مرة أخرى &curren; </p>\n" +
                            "      </div>\n";

                    html = htmlHead + sb + htmlFooter;
                    generateBill(html);
/*
                    mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
                    if (bm != null) {
                        // mPrinter.resetPrinter();
                        //  mPrinter.setHighIntensity();
                        // mPrinter.setAlignmentCenter();
                        // mPrinter.setBold();
                        btnPrint.setEnabled(false);
                        mPrinter.printDirect(getBitmapFromAsset("logo.png"), true, 200);
                        mPrinter.printDirect(bm, true, 200);
                        mPrinter.printLineFeed();
                        mPrinter.printLineFeed();
                        mPrinter.printLineFeed();
                        mPrinter.printLineFeed();
                        mPrinter.printLineFeed();
                        mPrinter.printLineFeed();
                        mPrinter.printLineFeed();
                    }
*/

                    html = "";
                    htmlHead = "";
                    // sb  = null;
                    htmlFooter = "";
                    bm = null;
                    btnPrint.setEnabled(true);
                    billList.clear();
                    billAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "SAVED", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "No BILL TO PRINT", Toast.LENGTH_LONG).show();
                }
                btnPrint.setEnabled(true);
                tvTotalPrice.setText("");
                tvTotalQty.setText("");
            }

        });

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvId = (TextView) view.findViewById(R.id.tv_id);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
                TextView tvArName = (TextView) view.findViewById(R.id.tv_arabic_name);

                tvName.playSoundEffect(android.view.SoundEffectConstants.CLICK);

                id = tvId.getText().toString().trim();
                name = tvName.getText().toString();
                price = tvPrice.getText().toString();
                arName = tvArName.getText().toString();

                addItemDialog(name);
            }
        });
        lvBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editItem = true;
                editItemId = i;

                TextView tvQty = (TextView) view.findViewById(R.id.et_qty);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                TextView tvPrice = (TextView) view.findViewById(R.id.tv_unit_price);
                TextView tvTotal = (TextView) view.findViewById(R.id.tv_price);

                tvName.playSoundEffect(android.view.SoundEffectConstants.CLICK);

                qty = tvQty.getText().toString().trim();
                name = tvName.getText().toString();
                price = tvPrice.getText().toString();
                itemTotal = tvTotal.getText().toString();
                addItemDialog(name);


            }
        });
       /* if (dbHelper.getItemsData().getCount() <= 0) {
            Calendar newCalendar = Calendar.getInstance();
            String time = dateFormatter.format(newCalendar.getTime()).toString();
            dbHelper.insertItemData("brst-01", "Broast Chicken Full", "BROAST FULL", "بروست", "26.00", "#dd4f49", time);
            dbHelper.insertItemData("brst-05", "Broast Chicken Half", "BROAST HALF", "بروست", "13.00", "#dd4f49", time);
            dbHelper.insertItemData("brst-025", "Broast Chicken Quarter", "BROAST QUARTER", "بروست", "7.00", "#dd4f49", time);
            dbHelper.insertItemData("brgr-01", "Burger Normal", "BURGER NORMAL", "برغر", "5.00", "#006a62", time);
            dbHelper.insertItemData("brgr-02", "Burger Double", "BURGER DOUBLE", "برغر", "10.00", "#006a62", time);
            dbHelper.insertItemData("kudu-01", "kudu", "KUDU", "كودو", "8.00", "#544e4b", time);
            dbHelper.insertItemData("kudu-05", "kudu half", "KUDU HALF", "كودو", "4.00", "#544e4b", time);
            dbHelper.insertItemData("trtla-01", "thirthilla big", "THURTHILLA BIG", "بروست", "10.00", "#40993a", time);
            dbHelper.insertItemData("trtla-05", "thirthilla Small", "THURTHILLA SMALL", "برغر", "5.00", "#40993a", time);
            dbHelper.insertItemData("juice-01", "fresh juices", "FRESH JUICE", "العصائر الطازجة", "5.00", "#d3236d", time);

        }
*/
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "No Bluetooth adapter", Toast.LENGTH_SHORT).show();
            //finish();
        }
        try {
            mPrinter.initService(ActivityMain.this, mMessenger);
            // mPrinter.showDeviceList(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPrinter.onActivityResume();
        if (dbHelper.getItemsData().getCount() > 0) {
            gridAdapter = new MainGridAdapter(getApplicationContext(), dbHelper.getItemsData());
            gvMain.setAdapter(gridAdapter);
        }
    }

    private void totalCalc() {
        // TOTAL VALUES
        double totalPrice = 0.0;
        float totalQty = 0;
        for (int i = 0; i < billList.size(); i++) {
            BillModel modal = billList.get(i);
            totalPrice += Double.valueOf(modal.price.trim());
            totalQty += Float.valueOf(modal.qty.trim());
        }
        tvTotalPrice.setText(String.valueOf(totalPrice) + "0");
        tvTotalQty.setText(String.valueOf(totalQty));
    }

    private void addBillItemModel(String id, String name, String arName, String price,
                                  String quantity, String totalPrice) {
        boolean isfound = false;
        for (int i = 0; i < billList.size(); i++) {

            if (billList.get(i).id.equalsIgnoreCase(id.trim())) {
                isfound = true;
                // itemRepeatDialog(name);
                BillModel modal = billList.get(i);
                String eq = modal.qty.trim();
                String eT = modal.price.trim();
                if (eq.equals(null) || eq.trim().equalsIgnoreCase(""))
                    eq = "0";
                if (eT.equals(null) || eT.trim().equalsIgnoreCase(""))
                    eT = "0";
               /* if (eq == null || eq.trim().equalsIgnoreCase(""))
                    eq = "0";
                if (eT == null || eT.trim().equalsIgnoreCase(""))
                    eT = "0";*/

                float eQuantity = Float.parseFloat(eq);
                float eTotal = Float.parseFloat(eT);
                String nq = quantity.trim();
                String nT = price.trim();
                if (nq.equals(null) || nq.trim().equalsIgnoreCase(""))
                    nq = "0";
                if (nT.equals(null) || nT.trim().equalsIgnoreCase(""))
                    nT = "0";
                float newQuantity = Float.parseFloat(nq);
                float newTotal = Float.parseFloat(nT);

                modal.qty = new String((eQuantity + newQuantity) + "");// modal.qty = new String((eQuantity + newQuantity) + "");
                if (modal.qty.endsWith(".0"))
                    modal.qty = modal.qty.substring(0, modal.qty.length() - 2);

                modal.price = new String((eTotal + newTotal) + "0"); //  modal.price = new String((eTotal + newTotal) + "0");

                break;
            }
        }
        if (!isfound) {
            BillModel newModel = new BillModel();
            newModel.id = id;
            newModel.name = name;
            newModel.arName = arName;
            newModel.qty = quantity;
            newModel.unitPrice = price;
            newModel.price = totalPrice;
            billList.add(newModel);
        }
        billAdapter = new BillListAdapter(this, billList, true);
        lvBill.setAdapter(billAdapter);
        billAdapter.setOnItemDeleteListener(this);
    }

    private void itemRepeatDialog(final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        AlertDialog dialog = builder.setMessage(title + " is already added to the bill, Please edit the quantity to add more ").
                setPositiveButton("  OK  ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.getWindow().setLayout(500, 160);
        dialog.show();
    }

    private void qtyConfrmDialog(final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        // builder.getWindow().setLayout(100, 100);
        AlertDialog dialog = builder.setMessage("Please Add a Quantity for " + title).
                setPositiveButton("  OK  ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.getWindow().setLayout(500, 160);
        dialog.show();

    }

    private void exitCnfrmDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        AlertDialog dialog = builder.setMessage("Do you want to exit").
                setPositiveButton("  OK  ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backUpDb();
                        //  finish();

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.getWindow().setLayout(500, 160);
        dialog.show();
    }

    @Override
    public void onDeleteListener(int position) {
        billList.remove(position);
        totalCalc();
        billAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                // "Swipe Right";
                mPrinter.showDeviceList(ActivityMain.this);
                //  mPrinter.initService(ActivityMain.this, mMessenger);
                //  mPrinter.connectToPrinter();
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                // "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                // "Swipe Down";

                break;
            case SimpleGestureFilter.SWIPE_UP:
                // "Swipe Up";
                Intent intent = new Intent(
                        ActivityMain.this, LoginDialogActivity.class);
                startActivity(intent);
                break;

        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    private void setStatusMsg(String msg) {
//        statusMsg.setText(msg);
        DebugLog.logTrace(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private Bitmap getBitmapFromAsset() {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("logo.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(istr);
    }


    private void generateBill(String html) {
        webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        final Handler mHandler = new Handler();


        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                webView.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                webView.setDrawingCacheEnabled(true);
                webView.buildDrawingCache();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        bm = Bitmap.createBitmap(576, //<-- WHERE IT BREAKS
                                webView.getContentHeight(), Bitmap.Config.ARGB_8888);

                        Canvas bigcanvas = new Canvas(bm);
                        Paint paint = new Paint();
                        int iHeight = bm.getHeight();

                        bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                        webView.draw(bigcanvas);
                        biilViewDialog("Bill No: " + billNo, bm);
                        //iv.setImageBitmap(bm);
                        btnPrint.setEnabled(true);

                    }

                }, 800);
            }
        });
    }

    private void biilViewDialog(String billNO, final Bitmap bmBill) {
        final AppCompatDialog dialog = new AppCompatDialog(this,R.style.MyAlertDialogStyle);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.print_dialog);
        final Button btnPrint = (Button) dialog.findViewById(R.id.btn_print);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final ImageView ivBill = (ImageView) dialog.findViewById(R.id.iv_bill);
        ivBill.setImageBitmap(bmBill);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
                if (bmBill != null) {
                    // mPrinter.resetPrinter(); mPrinter.setHighIntensity(); mPrinter.setAlignmentCenter(); mPrinter.setBold();
                    btnPrint.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Printing...", Toast.LENGTH_SHORT).show();
                    mPrinter.printDirect(getBitmapFromAsset(), false, 127);
                    mPrinter.printDirect(bmBill, true, 150);
                    storeImage(bmBill,"bill");
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    dialog.dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(532, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
    private boolean storeImage(Bitmap imageData, String filename) {
        //get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory() + "/myAppDir/myImages/";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }

        return true;
    }
    private void addItemDialog(String title) {
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setView(R.layout.add_bill_item_dialog);*/
        final AppCompatDialog dialog = new AppCompatDialog(this,R.style.MyAlertDialogStyle );
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(title);
        dialog.setContentView(R.layout.add_bill_item_dialog);
        final EditText et = (EditText) dialog.findViewById(R.id.et_qty);
        final TextView tvTotal = (TextView) dialog.findViewById(R.id.tv_total_amnt);
        final TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_price);
        tvPrice.setText(price);
        if (editItem) {
            tvTotal.setText(itemTotal);
            et.setText(qty);
            et.setSelection(qty.length());
        }
        Button btnDone = (Button) dialog.findViewById(R.id.btn_done);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);


        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    qty = et.getText().toString();

                    if (billList == null || billList.size() == 0) {
                        if (qty.trim().length() == 0 || qty.trim().equals("0")) {
                            Vibrator v1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v1.vibrate(300);
                            et.setBackgroundResource(R.drawable.et_bgrd_red);
                        } else {
                            nqty = Double.parseDouble(qty);
                            nprice = Double.parseDouble(price);
                            totalAmnt = (nqty * nprice) + "0"; //totalAmnt = new String((nqty * nprice) + "0");
                            addBillItemModel(id, name, arName, price, qty, totalAmnt);
                            totalCalc();
                            dialog.dismiss();
                        }
                        lastId = id;
                    } else {
                        for (int j = 0; j < billList.size(); j++) {
                            // if (billList.get(j).id.equalsIgnoreCase(lastId)) {
                            BillModel modal = billList.get(j);
                            if (modal.qty.equals("0")) {
                                qtyConfrmDialog(modal.name);
                            } else {
                                nqty = Double.parseDouble(qty);
                                nprice = Double.parseDouble(price);
                                totalAmnt = (nqty * nprice) + "0";//totalAmnt = new String((nqty * nprice) + "0");
                                if (!editItem)
                                    addBillItemModel(id, name, arName, price, qty, totalAmnt);
                                else {
                                    modal = billList.get(editItemId);
                                    modal.qty = qty;
                                    modal.price = tvTotal.getText().toString().trim();
                                    qty = "";
                                    billAdapter.notifyDataSetChanged();
                                    editItem = false;
                                }
                                lastId = id;
                                totalCalc();
                                dialog.dismiss();
                            }
                            break;
                            //   }
                        }
                    }
                }
                return false;
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && !charSequence.toString().equals("."))
                    tvTotal.setText((Double.parseDouble(charSequence.toString()) * Double.parseDouble(price)) + "0");
                    // tvTotal.setText(new String((Double.parseDouble(charSequence.toString()) * Double.parseDouble(price)) + "0"));
                else
                    tvTotal.setText((Double.parseDouble("0") * Double.parseDouble(price)) + "0");
                //tvTotal.setText(new String((Double.parseDouble("0") * Double.parseDouble(price)) + "0"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qty = et.getText().toString();
                double nqty, nprice;

                if (billList == null || billList.size() == 0) {
                    if (qty.length() == 0 || qty.trim().equals("0")) {
                        //qtyConfrmDialog(name);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(300);
                        et.setBackgroundResource(R.drawable.et_bgrd_red);

                    } else {
                        nqty = Double.parseDouble(qty);
                        nprice = Double.parseDouble(price);
                        totalAmnt = (nqty * nprice) + "0"; //totalAmnt = new String((nqty * nprice) + "0");
                        addBillItemModel(id, name, arName, price, qty, totalAmnt);
                        totalCalc();
                        dialog.dismiss();
                    }
                    lastId = id;
                } else {
                    for (int j = 0; j < billList.size(); j++) {
                        //if (billList.get(j).id.equalsIgnoreCase(lastId)) {
                        BillModel modal = billList.get(j);
                        if (modal.qty.equals("0")) {
                            qtyConfrmDialog(modal.name);
                        } else {
                            nqty = Double.parseDouble(qty);
                            nprice = Double.parseDouble(price);
                            totalAmnt = (nqty * nprice) + "0"; // totalAmnt = new String((nqty * nprice) + "0");
                            if (!editItem)
                                addBillItemModel(id, name, arName, price, qty, totalAmnt);
                            else {
                                modal = billList.get(editItemId);
                                modal.qty = qty;
                                modal.price = tvTotal.getText().toString().trim();
                                qty = "";
                                billAdapter.notifyDataSetChanged();
                                editItem = false;
                            }
                            lastId = id;
                            totalCalc();
                            dialog.dismiss();
                        }
                        break;
                        // }
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(423, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPrinter.onActivityResult(requestCode, resultCode, this);
    }

    @Override
    protected void onPause() {
        mPrinter.onActivityPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPrinter.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // do nothing.

        exitCnfrmDialog();
    }

    private void backUpDb() {

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd_MM_yy");
        String time = df.format(calendar.getTime());
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.fiume.billingmechine" + "/databases/BilllingMechineDB";
        String backupDBPath = "_BillingMechineDBBackup" + time; //"/EazyBiller/" +time+
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Backup Successful", Toast.LENGTH_LONG).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Backup Failed", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @SuppressLint("HandlerLeak")
    private class PrintSrvMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CieBluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case CieBluetoothPrinter.STATE_CONNECTED:
                            setStatusMsg(title_connected_to + mConnectedDeviceName);
                            break;
                        case CieBluetoothPrinter.STATE_CONNECTING:
                            setStatusMsg(title_connecting);
                            break;
                        case CieBluetoothPrinter.STATE_LISTEN:
                            setStatusMsg(title_connected_to + mConnectedDeviceName);
                        case CieBluetoothPrinter.STATE_NONE:
                            setStatusMsg(title_not_connected);
                            break;
                    }
                    break;
                case CieBluetoothPrinter.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(
                            CieBluetoothPrinter.DEVICE_NAME);
                    break;
                case CieBluetoothPrinter.MESSAGE_STATUS:
                    DebugLog.logTrace("Message Status Received");
                    setStatusMsg(msg.getData().getString(
                            CieBluetoothPrinter.STATUS_TEXT));
                    break;
                case CieBluetoothPrinter.PRINT_COMPLETE:
                    setStatusMsg("PRINT OK");
                    break;
                case CieBluetoothPrinter.PRINTER_CONNECTION_CLOSED:
                    setStatusMsg("Printer Connection closed");
                    break;
                case CieBluetoothPrinter.PRINTER_DISCONNECTED:
                    setStatusMsg("Printer Connection failed");
                    break;
                default:
                    //DebugLog.logTrace("Some un handled message : " + msg.what);
                    super.handleMessage(msg);
            }
        }
    }
}
