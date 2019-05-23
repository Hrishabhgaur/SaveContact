package com.example.hitman
import android.app.Activity
import android.app.Person
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import com.example.hitman.R
import  android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.display.view.*
import java.lang.Integer.parseInt
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MainActivity() : AppCompatActivity() {

 private var lstPersons= mutableListOf<Pperson>()

    internal lateinit var  db:Dbms
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
db=Dbms(this)
        refreshData()
        b1.setOnClickListener{
            val person=Pperson(
                Edit_text1.text.toString(),Edit_text2.text.toString()
            )
            db.addperson(person)
        refreshData()
        }
    }
    private fun refreshData(){
lstPersons = db.allPerson
        val adapter =Newer(this@MainActivity,lstPersons,Edit_text1,Edit_text2)
   list1.adapter=adapter
    }
}
open class Person {
    var name:String?=null
    var phone:Int=0
    constructor(){}

    constructor(Name:String,Phone:Int){
        this.name=Name
       this.phone=Phone
    }
}
data class Pperson(var name:String,var phone:String){

}
class Newer(internal  var activity: Activity,
            internal  var lstPerson:MutableList<Pperson>,
            internal  var Edit_text1:EditText,
            internal  var Edit_text2:EditText):BaseAdapter() {


    internal var inflater:LayoutInflater
    init{
        inflater=activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val rowView: View
        rowView = inflater.inflate(R.layout.display, null)



        rowView.Name.text = lstPerson[position].name.toString()
        rowView.Phone.text = lstPerson[position].phone.toString()

        rowView.setOnClickListener {
            Edit_text1.setText(rowView.Name.text.toString())
            Edit_text2.setText(rowView.Phone.text.toString())
        }
        return rowView
    }

    override fun getItem(position: Int): Any {
        return lstPerson[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return lstPerson.size
    }
//internal var inflater:LayoutInflater

}



open class Dbms (context:Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VAR){
    companion object {
        private val DATABASE_NAME="EDMTDTB.db"
        private val       DATABASE_VAR=1

        private val TABLE_NAME="PersonX"
        private val COL_NAME="Name"
        private val COL_PHONE="Phone"
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_NAME TEXT,$COL_PHONE TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY);
    }

    val allPerson: MutableList<Pperson>
        get() {
            val lstPersons = mutableListOf<Pperson>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor = db.rawQuery(selectQuery,null)
            if (cursor!=null&&cursor.moveToFirst()) {
                do {
var c=cursor.getString(cursor.getColumnIndex(COL_NAME))
                    if(c==null)
                        c="0"
                        val person = Pperson(c, cursor.getString(cursor.getColumnIndex(COL_PHONE)))
                        lstPersons.add(person)




                } while (cursor.moveToNext())
            }
            db.close()
            return lstPersons
        }
    fun addperson(person: Pperson){
        val p =Pperson(person.name,person.phone)
        val db=this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME,p.name)
        values.put(COL_PHONE,p.phone)
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

}




