package com.example.joem.introtoasynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Handler handlerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("demo", "onCreate thread ID is " + Thread.currentThread().getId());//print to logcat debug when created

        //creates 'doInBackground' and executes it via async; expects string, that is parameter passed to 'doInBackground'
        //enables us to control how much fake work we do inside, arbitrary-like number that we can control to determine how much fake work
        new DoWorkAsync().execute(1000000);
        //COMMENTED OUT the following line when we changed AsyncTask's first variable from 'string' to 'integer'
        //new DoWorkAsync().execute("Bob", "Alice");
    }

    //takes in 3 types (of our choosing)
    class DoWorkAsync extends AsyncTask<Integer, Integer, Double>{

        //the following three methods execute in the 'main thread;' overridden methods for this example to show where work is being done
        //can verify they're in 'main thread' by checking if ID=1 (meaning it's running in 'main thread')
        @Override
        protected void onPreExecute() {
            //print to logcat debug before creation
            Log.d("demo", "onPreExecute thread ID is " + Thread.currentThread().getId());
        }

        //takes last input from AysncTask (in this case type 'double'), which is returned from 'doInBackground'
        @Override
        protected void onPostExecute(Double aDouble) {
            //print to logcat debug after execution
            Log.d("demo", "onPostExecute thread ID is " + Thread.currentThread().getId());
            //the following line is to show that the value being returned by 'doInBackground' will appear in 'onPostExecute' (333.33)
            Log.d("demo", "onPostExecute aDouble is " + aDouble);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //print to logcat debug when progress updates
            Log.d("demo", "onProgressUpdate thread ID is " + Thread.currentThread().getId());
            //can publish multiple 'values' so 'values[0]' specifies only the one being sent
            Log.d("demo", "onProgressUpdate progress is " + values[0]);
        }

        //works in child thread in the background
        //uses the first input from AsyncTask (in this case type 'string')
        //Returns last input from AysncTask (in this case type 'double')
        //'...' means 'array;' params is an array
        @Override
        protected Double doInBackground(Integer... paramsName) { //paramsName=the name of the parameter
            //creating fake work to run in background or another thread
            double sum = 0;
            double  count = 0;
            Random randomName = new Random();
            for (int i=0; i<100; i++){
                for(int j=0; j<paramsName[0]; j++){//'paramsName[0]'=the one we're going to pass
                    count++;
                    sum = randomName.nextDouble() + sum;
                }
                //+1 to make it 1-100; takes this number and puts it in a que which is fetched by a handler that calls 'onProgressUpdate' with 'paramsName' value being deposited done through messaging
                publishProgress(i+1);
            }
            return sum/count;//returns average, which is ~0.5 because 'rand' produces number from 0-1
            //COMMENTED OUT when we changed AsyncTask's first variable from 'string' to 'integer'

            //looping over doInBackground progress to show that it's an array
            /*for (String var:paramsName) {
                Log.d("demo", "doInBackground params is " + var);
            }

            //print to logcat debug while in background
            Log.d("demo", "doInBackground thread ID is " + Thread.currentThread().getId());

            publishProgress(100); //publishes progress triggering onProgressUpdate to run

            return 333.33;//arbitrary double being returned*/
        }
    }
}
