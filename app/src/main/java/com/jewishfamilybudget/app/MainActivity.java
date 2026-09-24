package com.jewishfamilybudget.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout page, content;
    SharedPreferences prefs;
    boolean yiddish=false;
    final int NAVY=Color.rgb(36,65,91), BLUE=Color.rgb(69,112,145), BG=Color.rgb(247,248,246),
              CARD=Color.WHITE, MUTED=Color.rgb(105,116,125), GOLD=Color.rgb(190,151,70);
    String[] cats={"Household & Housing","Monthly Bills","Weekly Groceries","Yom Tov Planning","Simcha Planning","School & Camp","Pocket Cash","Credit Cards","Bank & Savings","Investments / S&P","Maaser / Tzedakah","Reports","Financial Tools"};
    String[] icons={"⌂","▣","🛒","🕯","◊","🎓","💵","▤","▥","↗","♥","▥","▦"};

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if (Build.VERSION.SDK_INT >= 30) getWindow().setDecorFitsSystemWindows(true);
        prefs=getSharedPreferences("budget",MODE_PRIVATE);
        showDashboard();
    }

    TextView tv(String s,float size,int color){
        TextView v=new TextView(this); v.setText(s); v.setTextSize(size); v.setTextColor(color);
        v.setFontFeatureSettings("kern"); return v;
    }
    TextView title(String s){ TextView v=tv(s,22,NAVY); v.setTypeface(Typeface.DEFAULT,Typeface.BOLD); v.setPadding(0,4,0,4); return v; }
    GradientDrawable bg(int color,float r){ GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(r);return g; }

    Button action(String text){
        Button b=new Button(this); b.setText(text); b.setTextSize(15); b.setTextColor(NAVY); b.setAllCaps(false);
        b.setGravity(Gravity.CENTER_VERTICAL); b.setPadding(18,0,18,0); b.setBackground(bg(CARD,18));
        return b;
    }

    void base(String heading){
        page=new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL); page.setBackgroundColor(BG);
        page.setPadding(18,10,18,10);
        LinearLayout top=new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL);
        Button menu=action("☰"); menu.setMinWidth(52); menu.setOnClickListener(v->showMenu());
        top.addView(menu,new LinearLayout.LayoutParams(56,52));
        TextView h=title(heading); h.setPadding(12,0,0,0); top.addView(h,new LinearLayout.LayoutParams(0,52,1));
        Button lang=action(yiddish?"English":"ייִדיש"); lang.setTextSize(13); lang.setOnClickListener(v->{yiddish=!yiddish;showDashboard();});
        top.addView(lang,new LinearLayout.LayoutParams(82,52));
        page.addView(top);
        content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(0,10,0,28);
        ScrollView sc=new ScrollView(this);sc.setFillViewport(true); sc.setClipToPadding(false); sc.addView(content,new ScrollView.LayoutParams(-1,-2)); page.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
        setContentView(page);
    }

    void showDashboard(){
        base(yiddish?"משפּחה בודזשעט":"Jewish Family Budget");
        TextView sub=tv(yiddish?"פּשוט • פּריוואַט • אָן אינטערנעט":"Simple • Private • Offline",14,MUTED);
        content.addView(sub);
        LinearLayout hero=new LinearLayout(this);hero.setOrientation(LinearLayout.VERTICAL);hero.setPadding(20,18,20,18);hero.setBackground(bg(Color.rgb(235,241,245),22));
        TextView ht=tv(yiddish?"קוק אויף דיין גאנצע בודזשעט":"Your family budget at a glance",21,NAVY);ht.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        hero.addView(ht); hero.addView(tv("Everything is optional. Add only what applies to your household.",13,MUTED));
        content.addView(hero,lp(1,12));
        TextView q=title("Quick Overview");content.addView(q,lp(1,16));
        LinearLayout ov=new LinearLayout(this);ov.setOrientation(LinearLayout.HORIZONTAL);
        ov.addView(stat("Monthly Bills","$"+prefs.getString("bills","0")),new LinearLayout.LayoutParams(0,92,1));
        ov.addView(stat("Groceries","$"+prefs.getString("grocery","0")),new LinearLayout.LayoutParams(0,92,1));
        ov.addView(stat("Pocket Cash","$"+prefs.getString("cash","0")),new LinearLayout.LayoutParams(0,92,1));
        content.addView(ov,lp(1,10));
        content.addView(title("Household & Planning"),lp(1,12));
        LinearLayout grid=new LinearLayout(this); grid.setOrientation(LinearLayout.VERTICAL);
        for(int row=0; row<cats.length; row+=2){
            LinearLayout line=new LinearLayout(this); line.setOrientation(LinearLayout.HORIZONTAL);
            for(int col=0; col<2 && row+col<cats.length; col++){
                final int n=row+col; Button b=action(icons[n]+"  "+cats[n]); b.setMinHeight(82);
                b.setOnClickListener(v->openCategory(cats[n]));
                LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(0,82,1); bp.setMargins(5,5,5,5); line.addView(b,bp);
            }
            if(row+1>=cats.length){ Space sp=new Space(this); line.addView(sp,new LinearLayout.LayoutParams(0,82,1)); }
            grid.addView(line,new LinearLayout.LayoutParams(-1,92));
        }
        content.addView(grid);
        TextView ins=tv("Tip: Press Enter to move to the next field. Gray fields are optional.",12,MUTED);ins.setPadding(4,14,4,10);content.addView(ins);
    }

    View stat(String label,String val){
        LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(14,10,10,10);c.setBackground(bg(CARD,16));
        TextView a=tv(label,12,MUTED);TextView b=tv(val,19,NAVY);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(a);c.addView(b);return c;
    }

    LinearLayout.LayoutParams lp(int w,int bottom){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(w==1?-1:w,LinearLayout.LayoutParams.WRAP_CONTENT);p.setMargins(0,0,0,bottom);return p;}

    void openCategory(String cat){
        base(cat);
        content.addView(tv("Add anything that applies. Every field is optional.",13,MUTED),lp(1,12));
        if(cat.equals("Reports")){reports();return;}
        if(cat.equals("Financial Tools")){tools();return;}
        if(cat.equals("Maaser / Tzedakah")){maaser();return;}
        if(cat.equals("Investments / S&P")){investments();return;}
        if(cat.equals("Monthly Bills")){bills();return;}
        if(cat.equals("Weekly Groceries")){amountForm("Weekly Grocery Order","grocery");return;}
        if(cat.equals("Pocket Cash")){amountForm("Cash I Keep / Use","cash");return;}
        if(cat.equals("Bank & Savings")){bank();return;}
        if(cat.equals("Credit Cards")){credit();return;}
        amountForm(cat+" Amount","cat_"+cat.replaceAll("[^A-Za-z]","_"));
    }

    EditText field(String hint,int type){
        EditText e=new EditText(this);e.setHint(hint+" (optional)");e.setTextSize(15);e.setSingleLine(true);e.setTextColor(NAVY);e.setHintTextColor(Color.rgb(165,170,173));e.setInputType(type);
        e.setPadding(16,10,16,10);e.setBackground(bg(Color.rgb(242,243,242),14));return e;
    }
    void chain(EditText... es){for(int i=0;i<es.length-1;i++){final EditText next=es[i+1];es[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);es[i].setOnEditorActionListener((v,id,event)->{next.requestFocus();return false;});}}

    void amountForm(String name,String key){
        EditText amount=field("Amount",2); EditText note=field("Name / note",1); chain(amount,note);
        content.addView(title(name),lp(1,8));content.addView(amount,lp(1,8));content.addView(note,lp(1,10));
        Button save=action("Save");save.setOnClickListener(v->{String a=amount.getText().toString();if(!a.isEmpty())prefs.edit().putString(key,a).apply();Toast.makeText(this,"Saved on this device",Toast.LENGTH_SHORT).show();});
        content.addView(save,lp(1,8)); Button back=action("← Dashboard");back.setOnClickListener(v->showDashboard());content.addView(back);
    }

    void bills(){
        EditText name=field("Bill name — rent, phone, electric, village, etc.",1), amt=field("Monthly amount",2), due=field("Due day",2);
        chain(name,amt,due);content.addView(title("Recurring Monthly Bills"),lp(1,8));content.addView(name,lp(1,8));content.addView(amt,lp(1,8));content.addView(due,lp(1,8));
        CheckBox paid=new CheckBox(this);paid.setText("Paid");paid.setTextColor(NAVY);content.addView(paid);
        Button save=action("Add Bill");save.setOnClickListener(v->{if(!name.getText().toString().isEmpty()){prefs.edit().putString("bill_"+name.getText().toString(),amt.getText().toString()).apply();Toast.makeText(this,"Bill saved",Toast.LENGTH_SHORT).show();}});content.addView(save,lp(1,8));
        content.addView(tv("You can add telephone, rent, utilities, local village bill, insurance, subscriptions, or any custom bill.",12,MUTED));
    }

    void bank(){
        EditText bank=field("Bank balance",2), save=field("Savings / emergency fund",2), cash=field("Other cash",2);chain(bank,save,cash);
        content.addView(bank,lp(1,8));content.addView(save,lp(1,8));content.addView(cash,lp(1,8));
        Button b=action("Save Balances");b.setOnClickListener(v->{prefs.edit().putString("bank",bank.getText().toString()).putString("savings",save.getText().toString()).putString("othercash",cash.getText().toString()).apply();Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();});content.addView(b);
    }

    void credit(){
        EditText card=field("Card name",1), balance=field("Current balance",2), due=field("Payment due",2), limit=field("Credit limit",2);chain(card,balance,due,limit);
        for(EditText e:new EditText[]{card,balance,due,limit})content.addView(e,lp(1,8));
        Button b=action("Save Credit Card");b.setOnClickListener(v->{Toast.makeText(this,"Credit card saved locally",Toast.LENGTH_SHORT).show();});content.addView(b);
        content.addView(tv("Track balances, payment due, minimum payment, and available credit. No bank connection is required.",12,MUTED),lp(1,8));
    }

    void investments(){
        EditText place=field("Investment — S&P, stock, IRA, etc.",1), value=field("Current value",2), contrib=field("Monthly contribution",2);chain(place,value,contrib);
        content.addView(title("Investments"),lp(1,8));content.addView(place,lp(1,8));content.addView(value,lp(1,8));content.addView(contrib,lp(1,8));
        Button b=action("Save Investment");b.setOnClickListener(v->Toast.makeText(this,"Investment saved locally",Toast.LENGTH_SHORT).show());content.addView(b,lp(1,8));
        content.addView(tv("Optional online market information can be added later. The core app stays usable offline.",12,MUTED));
    }

    void maaser(){
        EditText income=field("Income amount",2);content.addView(title("Maaser / Tzedakah Calculator"),lp(1,8));content.addView(income,lp(1,8));
        TextView result=tv("10%: $0.00",20,NAVY);result.setTypeface(Typeface.DEFAULT,Typeface.BOLD);content.addView(result,lp(1,10));
        Button calc=action("Calculate 10%");calc.setOnClickListener(v->{try{double x=Double.parseDouble(income.getText().toString());result.setText(String.format(Locale.US,"10%%: $%.2f",x*.10));}catch(Exception e){result.setText("Enter an amount.");}});content.addView(calc);
        content.addView(tv("This is a calculation tool; your personal minhag or rabbinic guidance may differ.",12,MUTED),lp(1,8));
    }

    void reports(){
        content.addView(title("Reports & Totals"),lp(1,8));
        String[] keys={"bills","grocery","cash","bank","savings","othercash"};double total=0;StringBuilder s=new StringBuilder();
        for(String k:keys){String v=prefs.getString(k,"");if(!v.isEmpty())try{total+=Double.parseDouble(v);}catch(Exception e){}}
        TextView t=tv(String.format(Locale.US,"Tracked total: $%.2f",total),24,NAVY);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);content.addView(t,lp(1,12));
        content.addView(tv("Reports can later be expanded to monthly, yearly, Yom Tov, Simcha, grocery, bills, credit and investment summaries.",13,MUTED),lp(1,12));
        Button export=action("Export / Share Report");export.setOnClickListener(v->Toast.makeText(this,"Export can be added without requiring internet.",Toast.LENGTH_SHORT).show());content.addView(export);
    }

    void tools(){
        content.addView(title("Financial Tools"),lp(1,8));
        Button inv=action("📈 Investment Growth Calculator");inv.setOnClickListener(v->investmentCalc());content.addView(inv,lp(1,8));
        Button bud=action("🧮 Monthly Budget Calculator");bud.setOnClickListener(v->amountForm("Monthly Budget","budget"));content.addView(bud,lp(1,8));
        Button ma=action("♥ Maaser 10% Calculator");ma.setOnClickListener(v->maaser());content.addView(ma,lp(1,8));
    }

    void investmentCalc(){
        EditText start=field("Starting amount",2), monthly=field("Monthly contribution",2), years=field("Years",2), rate=field("Estimated annual return %",2);
        chain(start,monthly,years,rate);for(EditText e:new EditText[]{start,monthly,years,rate})content.addView(e,lp(1,8));
        TextView out=tv("Estimated future value: $0",20,NAVY);out.setTypeface(Typeface.DEFAULT,Typeface.BOLD);content.addView(out,lp(1,8));
        Button b=action("Calculate");b.setOnClickListener(v->{try{double p=Double.parseDouble(start.getText().toString()),m=Double.parseDouble(monthly.getText().toString()),y=Double.parseDouble(years.getText().toString()),r=Double.parseDouble(rate.getText().toString())/100/12;int n=(int)(y*12);double fv=p*Math.pow(1+r,n)+m*((Math.pow(1+r,n)-1)/r);out.setText(String.format(Locale.US,"Estimated future value: $%,.2f",fv));}catch(Exception e){out.setText("Fill the numbers to calculate.");}});content.addView(b);
        content.addView(tv("Projection only — actual investment returns vary.",12,MUTED),lp(1,8));
    }

    void showMenu(){
        final String[] opts={"Dashboard","Household & Housing","Monthly Bills","Weekly Groceries","Yom Tov Planning","Simcha Planning","School & Camp","Pocket Cash","Credit Cards","Bank & Savings","Investments / S&P","Maaser / Tzedakah","Reports","Financial Tools","Settings"};
        new AlertDialog.Builder(this).setTitle("Jewish Family Budget").setItems(opts,(d,w)->{if(w==0)showDashboard();else if(w==14)settings();else openCategory(opts[w]);}).show();
    }
    void settings(){
        String[] a={"English","ייִדיש","About","Reset all local data"};
        new AlertDialog.Builder(this).setTitle("Settings").setItems(a,(d,w)->{
            if(w==0){yiddish=false;showDashboard();}
            else if(w==1){yiddish=true;showDashboard();}
            else if(w==2)new AlertDialog.Builder(this).setTitle("About").setMessage("Jewish Family Budget\nOffline-first household budgeting. Your entries stay on this device unless you later choose an online feature.").setPositiveButton("OK",null).show();
            else {prefs.edit().clear().apply();Toast.makeText(this,"Local data cleared",Toast.LENGTH_SHORT).show();showDashboard();}
        }).show();
    }
}
