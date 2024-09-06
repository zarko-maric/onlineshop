package zarko.maric.onlineshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import java.util.Collections;
public class dbHelper extends SQLiteOpenHelper{
    private final String TABLE_USER = "korisnik";
    private final String TABLE_ITEM = "itemCategory";
    private final String TABLE_HIST = "history";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "OnlineShop.db";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ADMIN = "isAdmin";

    public static final String ITEM_ID = "id";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_IMAGE = "image";
    public static final String ITEM_CATEGORY = "category";

    public static final String HIST_ID = "id";
    public static final String HIST_PRICE = "price";
    public static final String HIST_DATE = "date";
    public static final String HIST_STATUS = "status";
    public static final String HIST_USER = "user";

    private Context context;

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY , " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ADMIN + " INTEGER); ");

        db.execSQL("CREATE TABLE " + TABLE_ITEM + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY , " +
                ITEM_CATEGORY + " TEXT, " +
                ITEM_PRICE + " INTEGER, " +
                ITEM_NAME + " TEXT, " +
                ITEM_IMAGE + " TEXT); ");

        db.execSQL("CREATE TABLE " + TABLE_HIST + " (" +
                HIST_ID + " INTEGER PRIMARY KEY , " +
                HIST_DATE + " TEXT, " +
                HIST_STATUS + " TEXT, " +
                HIST_PRICE + " INTEGER, " +
                HIST_USER + " TEXT); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertUser(User user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ADMIN, user.isAdmin());

        long result = db.insert(TABLE_USER, null, values);
        close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertHistory(HistoryModel history){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HIST_DATE, history.getDate());
        values.put(HIST_STATUS, history.getStatus());
        values.put(HIST_PRICE, history.getPrice());
        values.put(HIST_USER, history.getUser());

        long result = db.insert(TABLE_HIST, null, values);
        close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_CATEGORY, item.getCategory());
        values.put(ITEM_IMAGE, item.getSlike());
        values.put(ITEM_PRICE, item.getCena());
        values.put(ITEM_NAME, item.getNaziv());

        long result = db.insert(TABLE_ITEM, null, values);
        close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void reducePricesByPercentage(int percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE items SET price = price - (price * ? / 100)";
        db.execSQL(updateQuery, new Object[]{percentage});
        db.close();
    }


    public void updatePassword(String username, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, newPassword);
        db.update(TABLE_USER,
                cv,
                COLUMN_USERNAME + " =?",
                new String[] {username});

        close();
    }

    public void deleteUser(String index) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_ID + " =?", new String[] {index});
        close();
    }

    public void deleteCategory(String category){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ITEM, ITEM_CATEGORY + "=?", new String[]{category});
        close();
    }

    public User[] readUsers(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, null , null, null, null, COLUMN_USERNAME + " ASC");

        if (cursor.getCount() <= 0) {
            return null;
        }
        User[] users = new User[cursor.getCount()];
        int i=0;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            users[i++] = createUser(cursor);
        }

        close();
        return users;
    }

    public User readUser(String username){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_USERNAME + " =?", new String[] {username}, null, null, null);

        if(cursor.getCount() <= 0){
            return null;
        }

        cursor.moveToFirst();
        User user = createUser(cursor);

        close();
        return user;
    }

    public HistoryModel[] readHistory(String username){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIST, null, HIST_USER + " =?",new String[] {username} , null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }
        HistoryModel[] history = new HistoryModel[cursor.getCount()];
        int i=0;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            history[i++] = createHistory(cursor);
        }

        close();
        return history;
    }

    public Item[] readItems(String category){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        if(category.equals("all")){
            cursor = db.query(TABLE_ITEM, null, null, null, null, null, null);
        }else {
            cursor = db.query(TABLE_ITEM, null, ITEM_CATEGORY + " =?", new String[]{category}, null, null, null);
        }

        if (cursor.getCount() <= 0) {
            return null;
        }
        Item[] items = new Item[cursor.getCount()];
        int i=0;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            items[i++] = createItem(cursor);
        }

        close();
        return items;
    }

    private User createUser(Cursor cursor){
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));

        String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));

        String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

        boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADMIN))==1;

        return new User(username, email, password, isAdmin);
    }

    public String[] readCategories() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + ITEM_CATEGORY + " FROM " + TABLE_ITEM, null) ;

        if(cursor.getCount() <= 0) {
            return null;
        }

        String[] categories = new String[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            categories[i++] = cursor.getString(0);
        }

        return categories;
    }


    private Item createItem(Cursor cursor){
        int price = cursor.getInt(cursor.getColumnIndexOrThrow(ITEM_PRICE));

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_NAME));

        String image = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_IMAGE));

        String category = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_CATEGORY));

        return new Item(price, name, image, category);
    }

    private HistoryModel createHistory(Cursor cursor){
        String date = cursor.getString(cursor.getColumnIndexOrThrow(HIST_DATE));

        String status = cursor.getString(cursor.getColumnIndexOrThrow(HIST_STATUS));

        int price = cursor.getInt(cursor.getColumnIndexOrThrow(HIST_PRICE));

        String user = cursor.getString(cursor.getColumnIndexOrThrow(HIST_USER));

        return new HistoryModel(date, status, price, user);
    }
}
