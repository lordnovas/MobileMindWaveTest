package jr.com.mindapp;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;




public class MindActivity extends ActionBarActivity {

    TGDevice mTGDevice;
    BluetoothAdapter mBtAdapter;
    TextView mTVpoor_signal;
    TextView mTVattention_value;
    TextView mTVmeditation_value;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mind_activity_layout);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBtAdapter !=null)
        {
           mTGDevice = new TGDevice(mBtAdapter,handler);
           mTGDevice.connect(true);
           mTVpoor_signal =  (TextView) findViewById(R.id.poor_signal);
           mTVmeditation_value = (TextView)findViewById(R.id.meditation_value);
           mTVattention_value = (TextView)findViewById(R.id.attention_value);
        }

    }

    private final Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case TGDevice.MSG_STATE_CHANGE:
                    switch (msg.arg1)
                    {
                        case TGDevice.STATE_IDLE:
                            break;

                        case TGDevice.STATE_CONNECTING:
                            break;
                        case TGDevice.STATE_CONNECTED:
                            mTGDevice.start();

                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            mTGDevice.close();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                        case TGDevice.STATE_NOT_PAIRED:
                            default:
                                break;
                    }
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    Log.d("Hello EEG ", "Poor Signal: " + msg.arg1);
                    mTVpoor_signal.setText("This is the poor Signal Value " + msg.arg1);
                case TGDevice.MSG_ATTENTION:
                    Log.d("Hello EEG ", "Attention: " + msg.arg1);
                    mTVattention_value.setText("This is the Attention value " + msg.arg1);
                    break;
                case TGDevice.MSG_RAW_DATA:
                    int rawValue = msg.arg1;
                    Log.v("Hello EEG ", "Raw Values " + rawValue );
                    break;
                case TGDevice.MSG_MEDITATION:
                    Log.d("Hello EEG ", "Meditation: " + msg.arg1 );
                    mTVmeditation_value.setText("This is the Meditation value "+ msg.arg1);
                    break;
                case TGDevice.MSG_EEG_POWER:  //add to screen
                     TGEegPower ep = (TGEegPower) msg.obj;
                     Log.d("Hello EEG ", "Delta " + ep.delta);
                     Log.d("Hello EEG ", "Theta " + ep.theta);
                     Log.d("Hello EEG ", "Low Alpha " + ep.lowAlpha);
                     Log.d("Hello EEG ", "High Alpha " + ep.highAlpha);
                     Log.d("Hello EEG ", "Low Beta " + ep.lowBeta);
                     Log.d("Hello EEG ", "High Beta " + ep.highBeta);
                     Log.d("Hello EEG ", "Low Gamma " + ep.lowGamma);
                     Log.d("Hello EEG ", "Mid Gamma " + ep.midGamma);
                    default:
                        break;
            }

        }


    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mind, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
