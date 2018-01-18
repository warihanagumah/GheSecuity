package com.example.warihana.ghesecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
/**
 * Created by Warihana on 12/5/2017. */

public class BackgroundTask extends AsyncTask<String, Void, String> {
    public static Context context;
    public static String status;

   public BackgroundTask(Context etx)
   {
      this.context = etx;
    }


    @Override
    protected String doInBackground(String... voids) {
       String type = voids[0];


       String insert_url = "https://ghesecurity.000webhostapp.com/MobileWebProject/useMethods.php";
       String register_url = "https://ghesecurity.000webhostapp.com/MobileWebProject/validateRegister.php";
       String login_url =  "https://ghesecurity.000webhostapp.com/MobileWebProject/validateLogin.php";
       //String login_url =  "http://172.20.19.96/MobileWebProject/validateLogin.php";

       if(type.equals("insert")){
           try {
               Log.d("ONTRYEXECUTE", "execting try background");
               String description = voids[1];
               String category = voids[2];
               String image = voids[3];
               String location = voids[4];

               URL url = new URL(insert_url);
               HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setDoOutput(true);
               httpURLConnection.setDoInput(true);
               Log.d("ONEXECUTE", "execting background");
               OutputStream outputStream = httpURLConnection.getOutputStream();
               BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
               String post_data = URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8")+"&"+
                       URLEncoder.encode("category", "UTF-8")+"="+URLEncoder.encode(category, "UTF-8")+"&"+
                       URLEncoder.encode("image", "UTF-8")+"="+URLEncoder.encode(image, "UTF-8")+"&"+
                       URLEncoder.encode("location", "UTF-8")+"="+URLEncoder.encode(location, "UTF-8");;
               Log.d("ONAFTEREXECUTE", "execting After background");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
               InputStream inputStream = httpURLConnection.getInputStream();
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
               Log.d("INPUTSTREAMLOG", "execting After background");
               String result = "";
               String line = "";
               while((line = bufferedReader.readLine())!=null) {
                   result += line;
               }
               bufferedReader.close();
               inputStream.close();
               httpURLConnection.disconnect();
               return result;

           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }

       }else if(type.equals("register")){
           try {
               Log.d("ONTRYEXECUTE", "execting try background");
               String email = voids[1];
               String password = voids[2];
               String phone = voids[3];
               String age = voids[4];
               String name = voids[5];

               URL url = new URL(register_url);
               HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setDoOutput(true);
               httpURLConnection.setDoInput(true);
               Log.d("ONEXECUTE", "execting background");
               OutputStream outputStream = httpURLConnection.getOutputStream();
               BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
               String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+
                       URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"+
                       URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(phone, "UTF-8")+"&"+
                       URLEncoder.encode("age", "UTF-8")+"="+URLEncoder.encode(age, "UTF-8")+"&"+
                       URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8");

               bufferedWriter.write(post_data);
               bufferedWriter.flush();
               bufferedWriter.close();
               outputStream.close();
               InputStream inputStream = httpURLConnection.getInputStream();
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
               String result = "";
               String line = "";
               while((line = bufferedReader.readLine())!=null) {
                   result += line;
               }
               bufferedReader.close();
               inputStream.close();
               httpURLConnection.disconnect();
               return result;

           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }

       }else if(type.equals("login")){
            try {
                Log.d("ONTRYEXECUTE", "execting try background");
                String email = voids[1];
                String password = voids[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                Log.d("INPUTSTREAMLOG", "execting After background");
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine())!=null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
       super.onPostExecute(result);
       if(result !=null){
           Log.d("POSTEXECUTE", result + " ");
           status=result;
           if (result.toLowerCase().contains("successLogin".toLowerCase())){
               SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("status", status);
               editor.apply();

               Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show();
               Intent dshboard = new Intent(context, Dashboard.class);
               dshboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(dshboard);
           }

           if (result.toLowerCase().contains("successful".toLowerCase())) {
               Toast.makeText(context, "Registration Successful", Toast.LENGTH_LONG).show();
               Intent login = new Intent(context, Login.class);
               login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(login);
           }

           if (result.toLowerCase().contains("reportsent".toLowerCase())){
               Toast.makeText(context, "Report sent", Toast.LENGTH_LONG).show();
           }
        }else {
           Toast.makeText(context, "Not successful", Toast.LENGTH_LONG).show();
           Log.d("POSTEXECUTE", "no response");
       }

    }


    @Override
    protected void onPreExecute() {
    Log.d("PREEXECUTE", "now preparing asyn task");

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
