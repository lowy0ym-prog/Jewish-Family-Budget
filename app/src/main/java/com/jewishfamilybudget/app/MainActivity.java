package com.jewishfamilybudget.app;
import android.app.*;import android.os.*;import android.graphics.Color;import android.view.*;import android.widget.*;import java.util.*;
public class MainActivity extends Activity{
 LinearLayout root,content; ArrayList<String> items=new ArrayList<>(); TextView total;
 public void onCreate(Bundle b){super.onCreate(b); build();}
 TextView t(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(Color.rgb(35,55,70));v.setPadding(28,22,28,22);return v;}
 void build(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(18,18,18,18); root.addView(t("✡  Jewish Family Budget",24)); root.addView(t("Simple • Private • Offline",14));
 LinearLayout grid=new LinearLayout(this);grid.setOrientation(LinearLayout.VERTICAL); String[] a={"🏠 Dashboard","🏡 Household & Housing","🧾 Monthly Bills","🛒 Weekly Groceries","🕯️ Yom Tov Planning","💍 Simcha Planning","🎓 School & Camp","💵 Pocket Cash","💳 Credit Cards","🏦 Bank & Savings","📈 Investments / S&P","❤️ Maaser / Tzedakah","📊 Reports","🧮 Financial Tools","⚙️ Settings"};
 for(String s:a){Button x=new Button(this);x.setText(s);x.setAllCaps(false);x.setOnClickListener(v->open(s));grid.addView(x,new LinearLayout.LayoutParams(-1,-2));} root.addView(grid,new LinearLayout.LayoutParams(-1,0,1)); total=t("",14);root.addView(total);setContentView(root);}
 void open(String s){ if(s.contains("Settings")){settings();return;} final EditText e=new EditText(this);e.setHint("Enter amount / notes (optional)");e.setInputType(2|8192); new AlertDialog.Builder(this).setTitle(s).setMessage("Everything is optional. Add what applies to your household.").setView(e).setNegativeButton("Back",null).setPositiveButton("Save",(d,w)->{String v=e.getText().toString(); if(!v.isEmpty())items.add(s+": $"+v); total.setText("Saved items: "+items.size());}).show();}
 void settings(){new AlertDialog.Builder(this).setTitle("Settings").setItems(new String[]{"English","ייִדיש","About","Reset local data"},(d,w)->{if(w==3){items.clear();total.setText("Saved items: 0");}}).show();}
}
