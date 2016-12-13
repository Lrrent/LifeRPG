package com.example.da.liferpg;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.da.liferpg.database.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    Button login;
    CardView cv;
    FloatingActionButton addChange;
    SharedPreferences LoginedJudg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllView();
        addChange.setOnClickListener(new myClickListener());
        login.setOnClickListener(new myClickListener());
    }
    private void findAllView(){
        addChange = (FloatingActionButton) findViewById(R.id.fab);
        login = (Button) findViewById(R.id.login);
        cv = (CardView) findViewById(R.id.cv);
        userName = (EditText) findViewById(R.id.et_username);
        userPassword = (EditText) findViewById(R.id.et_password);
    }
    class DBAsyncTask extends AsyncTask<String,Integer,String>{  //�����ⲿ���ݿ��漰����,���Ա�������һ���߳���ִ��
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            DataBase dataBase = new DataBase();  //���ӵ����ݿ���
            dataBase.connect();
            /*-------------------- ����˺ź������Ƿ���ȷ------------------------*/
            try {
                boolean exited = dataBase.QueryExisted(name);
                if (!exited){   //�������û���
                    return "notExited";
                }
                else{
                    ResultSet rs = dataBase.Query("select * from user where userName='"+name+"';");
                    while (rs.next()){
                        //Log.i(rs.getString("userId"), "doInBackground: ");
                        String nameDB = rs.getString("userName");
                        String passwordDB =rs.getString("userPassword");
                        if (name.equals(nameDB) && password.equals(passwordDB)){ //���Ե�¼
                            return "logined,"+name+","+password;
                        }
                        else if (!password.equals(passwordDB)){ //�������
                            return "wrongPassword";
                        }
                    }
                    dataBase.disConnect();   //�Ͽ����ݿ�����
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if (s.equals("notExited")){ //�û���������
                Toast.makeText(getApplicationContext(),"�û�������",Toast.LENGTH_LONG).show();

               // SnackbarUtil.LongSnackbar(userName,"�û���������",0xff2195f3,0xffffffff).show();
            }
            else if (s.equals("wrongPassword")){
                userPassword.setError("�������");
            }
            else if (s.split(",")[0].equals("logined")){ //�����½
                LoginedJudg = getSharedPreferences("user",MODE_PRIVATE);  //���Ե�¼֮����sharepreference��д������,�´ξͲ���Ҫ�ٵ�½
                SharedPreferences.Editor editor = LoginedJudg.edit();
                String loginFlag = "logined";
                editor.putString("logined",loginFlag);
                editor.putString("userName",s.split(",")[1]);
                editor.putString("userPassword",s.split(",")[2]);
                editor.commit();
                Explode explode = new Explode();
                explode.setDuration(500);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i2 = new Intent(LoginActivity.this,main_window.class);
                startActivity(i2, oc2.toBundle());
            }
        }
    }
    class myClickListener implements View.OnClickListener {   //��ť�ļ����¼�
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab:   //ת����ע�����
                        getWindow().setExitTransition(null);
                        getWindow().setEnterTransition(null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, addChange, view.getTransitionName());
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                    break;
                case R.id.login:
                        String name = userName.getText().toString();
                        String password = userPassword.getText().toString();
                        if(checkInput(name,password)){ //�����벻Ϊ��
                            //��Ҫ���ʵ����ݿ�,����ʹ���½�һ���߳�
                             checkedInsert(name,password);  //�ж��Ƿ��ܹ��������ݿ�,�����,�������ݿ���֮��Ϳ�����ת��������
                        }
                    break;
            }
        }
        private boolean checkedInsert(String name, String password){ //����������ݿ�,ͬʱ���������û����Ƿ���ȷ
            boolean inserted = true;
            new DBAsyncTask().execute(name,password);
            return inserted;
        }
        private boolean checkInput(String name,String password){ //��������Ƿ���ȷ(Ϊ��)
            boolean checked = true;
            if (name.equals("")){ //�û���Ϊ��ʱ
                userName.setError("�û���Ϊ��");
                checked = false;
            }
            if (password.equals("")){ //����Ϊ��
                userPassword.setError("���벻��Ϊ��");
                checked = false;
            }
            return checked;
        }
    }
}
