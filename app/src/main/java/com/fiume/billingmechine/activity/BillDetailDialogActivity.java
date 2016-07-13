package com.fiume.billingmechine.activity;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cie.btp.CieBluetoothPrinter;
import com.cie.btp.DebugLog;
import com.cie.btp.PrinterWidth;
import com.fiume.billingmechine.R;
import com.fiume.billingmechine.adapter.BillListAdapter;
import com.fiume.billingmechine.db.DatabaseHelper;
import com.fiume.billingmechine.fragments.BillFragment;
import com.fiume.billingmechine.model.BillModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Razi on 3/24/2016.
 */
public class BillDetailDialogActivity extends AppCompatActivity {
    private static final String title_connecting = "connecting...";
    private static final String title_connected_to = "connected: ";
    private static final String title_not_connected = "not connected";
    private static List<BillModel> billList;
    private static final CieBluetoothPrinter mPrinter = CieBluetoothPrinter.INSTANCE;
    final Messenger mMessenger = new Messenger(new PrintSrvMsgHandler());
    private ListView lvBill;
    private BillListAdapter billAdapter;
    private DatabaseHelper dbHelper;
    private String mConnectedDeviceName = "";
    private TextView tvTotalPrice;
    private TextView tvTotalQty;
    private WebView webView;
    private Button  btnPrint;
    private Button btnCancel;
    private SimpleDateFormat dateFormatter;
    private String html;
    private String htmlContent;
    private String htmlHead;
    private String htmlFooter;
    String id;
    String name;
    String price;
    String qty;
    String arName;
    private String billNo;
    String totalAmnt;
    String itemTotal;;
    private Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail_dialog);

        /*BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "No Bluetooth adapter", Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            mPrinter.initService(BillDetailDialogActivity.this, mMessenger);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        dbHelper = new DatabaseHelper(this);

        dateFormatter = new SimpleDateFormat("MMM dd,yyyy  hh:mm:ss a",
                Locale.US);

        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvTotalQty = (TextView) findViewById(R.id.tv_total_qty);
        btnPrint = (Button) findViewById(R.id.btn_print);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        lvBill = (ListView) findViewById(R.id.lv_bill);

        billList = dbHelper.getBillDetailData(BillFragment.billId);
        billAdapter = new BillListAdapter(this, billList, false);
        lvBill.setAdapter(billAdapter);

        tvTotalPrice.setText(dbHelper.getTotalAmntofBill(BillFragment.billId));
        tvTotalQty.setText(dbHelper.getTotalQtyofBill(BillFragment.billId));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            /* //   Calendar newCalendar = Calendar.getInstance();
               // String time = dateFormatter.format(newCalendar.getTime()).toString();

                if (billList.size() > 0) {
                  //  dbHelper.insertBillHeaderData(time, tvTotalPrice.getText().toString(), tvTotalQty.getText().toString(), );
                }*/

                StringBuilder sb = new StringBuilder("");//StringBuffer

                for (int i = 0; i < billList.size(); i++) {
                    BillModel model = billList.get(i);
                    htmlContent = "<tr>\n" +
                            "              <td>" + model.getPrice() + "</td>\n" +
                            "              <td>" + model.getUnitPrice() + "</td>        \n" +
                            "              <td>" + model.getQty() + " </td>\n" +
                            "              <td>" + model.getArName() + "</td>\n" +
                            "            </tr>\n";
                    sb.append(htmlContent);
                  //  dbHelper.insertBillDetailData(String.valueOf(dbHelper.getLastBillId()), model.getId(), model.getPrice(), model.qty);
                }

                billNo = String.valueOf(dbHelper.getLastBillId());
                if (billNo.length() == 1)
                    billNo = "00" + String.valueOf(dbHelper.getLastBillId());
                else if (billNo.length() == 2)
                    billNo = "0" + String.valueOf(dbHelper.getLastBillId());

                htmlHead = " <head>\n" +
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
                        "        border-bottom: 2px dotted #000;\n" +
                        "        border-top: 2px dotted #000;        \n" +
                        "      }\n" +
                        "      thead tr th, tfoot tr td, tbody tr td{        \n" +
                        "        text-align: right;\n" +
                        "        padding: 5px;\n" +
                        "      }\n" +
                        "      thead tr th, tfoot tr td{\n" +
                        "        font-weight: bold;\n" +
                        "      }\n" +
                        "      thead tr th, tfoot tr td, tbody tr td{\n" +
                        "       font-weight: bold;" +
                        "        font-size: 18px;\n" +
                        "\n" +
                        "      }\n" +
                        "\n" +
                        "      tbody tr td:last-child, tfoot tr td:last-child, p strong{\n" +
                        "        font-weight: bold;\n" +
                        "        font-size: 18px;\n" +
                        "      }\n" +
                        "      tbody tr:first-child td {\n" +
                        "        padding-top: 16px;\n" +
                        "      }\n" +
                        "      tbody tr:last-child td {\n" +
                        "        padding-bottom: 16px;\n" +
                        "      }\n" +
                        "\n" +
                        "      </style>\n" +
                        "\n" +
                        "\t\n" +
                        "\n" +
                        "      <div class=\"bill\">\n" +
                        "        <p>Street No.4 Sarafiya, Jedha <br/>\n" +
                        "        Tel:050564524, Email:mail@alamanabroast.com</p>\n" +
                        "        <p style=\"text-align: left\">Bill No: " + billNo + "</p>\n" +
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
                        "          <tbody>\n";


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
                        "                  <p><strong>" + tvTotalPrice.getText().toString() + "  المجموع الإجمالي</strong></p>\n" +
                        "                  <p>&curren; شكرا لك، و زيارة مرة أخرى &curren; </p>\n" +
                        "      </div>\n";

                html = htmlHead + sb + htmlFooter;
                generateBill(html);
                mPrinter.setPrinterWidth(PrinterWidth.PRINT_WIDTH_72MM);
                if (bm != null) {
                    mPrinter.printDirect(getBitmapFromAsset(), true, 0);
                    mPrinter.printDirect(bm, true, 0);
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                    mPrinter.printLineFeed();
                }

            }
        });
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
                        // imageView.setImageBitmap(bm);
                    }

                }, 1000);
            }
        });
    }

    private void setStatusMsg(String msg) {
//        statusMsg.setText(msg);
        DebugLog.logTrace(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
                    DebugLog.logTrace("Some un handled message : " + msg.what);
                    super.handleMessage(msg);
            }
        }
    }

}
