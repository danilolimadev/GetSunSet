package com.example.getsunset

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun GetSunSet(view: View){
        var city=etCityName.text.toString()
        //Replace your city in URL for city
        val url = "PutYourUrlHere"+city+"EndOfYourUrl"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //Before task started
        }
        override fun doInBackground(vararg params: String?): String {
            try{
                var url=URL(params[0])
                val urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=7000
                var inString=ConvertStreamToString(urlConnect.inputStream)
                //Cannot access to ui
                publishProgress(inString)
                return " "
            }catch (ex:Exception){
                return " "
            }
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json=JSONObject(values[0])
                val query=json.getJSONObject("query")
                val results=query.getJSONObject("results")
                val channel=results.getJSONObject("channel")
                val astronomy=channel.getJSONObject("astronomy")
                var sunrise=astronomy.getString("sunrise")
                tvSunSetTime.setText("Sunrise time is $sunrise")
            }catch (ex:Exception){

            }
        }

        override fun onPostExecute(result: String?) {
            //After task done
        }
    }

    fun ConvertStreamToString(inputStream: InputStream):String{
        val bufferReader=BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString:String=""

        try{
            do {
                line=bufferReader.readLine()
                if(line!=null){
                    AllString+=line
                }
            }while (line!=null)
            inputStream.close()
        }catch (ex:Exception){

        }

        return AllString
    }
}
