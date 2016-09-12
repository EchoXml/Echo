package aohan.zl.echo.echo.util;

import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

/**
 * Created by Echo on 2016/9/9.
 */
public  class NfcUtil implements NfcAdapter.CreateBeamUrisCallback {

    @Override
    public  Uri[] createBeamUris(NfcEvent event) {
        return new Uri[0];
    }
}
