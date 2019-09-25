package com.example.protocolbuffer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.protocolbuffer.bean.PersonBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        testProtocolBuffer();
    }

    private void testProtocolBuffer() {
        // 步骤1:通过 消息类的内部类Builder类 构造 消息类的消息构造器
        PersonBean.Person.Builder personBean = PersonBean.Person.newBuilder();

        // 步骤2:设置你想要设置的字段为你选择的值
        personBean.setName("chtlei");
        personBean.setId(1001);
        personBean.setEmail("1234@4321");

        PersonBean.Person.PhoneNumber.Builder phoneNumber = PersonBean.Person.PhoneNumber.newBuilder();
        phoneNumber.setType(PersonBean.Person.PhoneType.MOBILE);
        phoneNumber.setNumber("123456789");
        // PhoneNumber消息是嵌套在Person消息里,可以理解为内部类
        // 所以创建对象时要通过外部类来创建
        PersonBean.Person.PhoneNumber phoneNumber1 = phoneNumber.build();
        personBean.addPhone(phoneNumber1);

        // 步骤3:通过 消息构造器 创建 消息类 对象
        PersonBean.Person person = personBean.build();

        // 步骤4:序列化和反序列化消息(两种方式)

        /*方式1：直接 序列化 和 反序列化 消息 */
        // a.序列化
        // 把 person消息类对象 序列化为 byte[]字节数组
        byte[] byteArray1 = person.toByteArray();
        // 查看序列化后的字节流
        Log.i("LCT",Arrays.toString(byteArray1));

        // b.反序列化
        try {
            // 当接收到字节数组byte[] 反序列化为 person消息类对象
            PersonBean.Person personResponse = PersonBean.Person.parseFrom(byteArray1);

            // 输出反序列化后的消息
            Log.i("LCT","反序列化之后 name is----->" + personResponse.getName());
            Log.i("LCT","反序列化之后 id is----->" + personResponse.getId());
            Log.i("LCT","反序列化之后 email is----->" + personResponse.getEmail());
            Log.i("LCT","反序列化之后 phone is----->" + personResponse.getPhoneList().get(0).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*方式2：通过输入/ 输出流（如网络输出流） 序列化和反序列化消息 */
        // a.序列化
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            // 将消息序列化 并写入 输出流(此处用 ByteArrayOutputStream 代替)
            person.writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 通过 输出流 转化成二进制字节流
        byte[] byteArray = output.toByteArray();

        // b. 反序列化
        // 通过 输入流 接收消息流(此处用 ByteArrayInputStream 代替)
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
        try {
            PersonBean.Person personResponse = PersonBean.Person.parseFrom(input);

            // 输出反序列化后的消息
            Log.i("LCT","反序列化之后 name is----->" + personResponse.getName());
            Log.i("LCT","反序列化之后 id is----->" + personResponse.getId());
            Log.i("LCT","反序列化之后 email is----->" + personResponse.getEmail());
            Log.i("LCT","反序列化之后 phone is----->" + personResponse.getPhoneList().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        mTextView = findViewById(R.id.tv_test);
    }
}
