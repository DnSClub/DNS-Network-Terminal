package com.lumpology.nfcterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;

import com.lumpology.nfcterminal.UdpClient;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView nfcInfoTextView;

    private EditText DNS_BANK_MERCH_PRICE;
    private EditText DNS_NETWORK;
    private  EditText passwordEditText;

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcInfoTextView = findViewById(R.id.nfcInfoTextView);
        DNS_BANK_MERCH_PRICE = findViewById(R.id.DNS_BANK_MERCH_PRICE);
        passwordEditText = findViewById(R.id.passwordEditText);
        DNS_NETWORK = findViewById(R.id.DNS_NETWORK);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableNfcForegroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableNfcForegroundDispatch();
    }

    private void enableNfcForegroundDispatch() {
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableNfcForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // Handle the NFC tag data here
            processNfcTag(intent);
        }
    }

    private void processNfcTag(Intent intent) {
        // Extract NFC tag data and update the UI
        // Example: Read NDEF messages from the tag
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        ArrayList<String> extracted_data = new ArrayList<String>();
        String tagData = "";
        if (rawMessages != null && rawMessages.length > 0) {
            StringBuilder tagDataBuilder = new StringBuilder();

            for (Parcelable rawMessage : rawMessages) {
                NdefMessage message = (NdefMessage) rawMessage;
                NdefRecord[] records = message.getRecords();

                for (NdefRecord record : records) {
                    String payload = new String(record.getPayload());
                    tagDataBuilder.append(payload);
                }
            }

            tagData = tagDataBuilder.toString();
            extracted_data.add(tagData);
            nfcInfoTextView.setText(tagData);
        } else {
            nfcInfoTextView.setText("No Data Messages Found On The DNS Card.");
        }
        String converted_extracetd_data = DNS_NETWORK.getText().toString()+"\nExtracted Payload: {\nCard Data: {"+nfcInfoTextView.getText().toString()+"}\nPrice: {"+ DNS_BANK_MERCH_PRICE.getText().toString()+"}\nVendor: {"+ passwordEditText.getText().toString()+"}\n}";
        //Toast.makeText(this, "Gots:"+converted_extracetd_data, Toast.LENGTH_SHORT).show();
        //UdpClientCaller.callUdpClientInBackground(converted_extracetd_data);

        //this triggers the UDP sender and accepts a string array to be sent to the server
        tagData = UdpClient.card_sender(converted_extracetd_data);
        Toast.makeText(this, tagData, Toast.LENGTH_SHORT).show();
        nfcInfoTextView.setText(tagData);
    }
}