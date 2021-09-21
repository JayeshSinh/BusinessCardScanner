package com.business.card.scanner.maker.database.dao;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Log;

import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;


import com.business.card.scanner.maker.model.Business_Card;
import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("RestrictedApi")
public final class Database_Dao_Impl implements Database_Dao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBusiness_Card;
    private final EntityInsertionAdapter __insertionAdapterOfBusiness_Card;
    private final SharedSQLiteStatement __preparedStmtOfRemoveGroup;
    private final SharedSQLiteStatement __preparedStmtOfSetFav;
    private final SharedSQLiteStatement __preparedStmtOfUpdateGroup;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfBusiness_Card;


    public Database_Dao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfBusiness_Card = new EntityInsertionAdapter<Business_Card>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `Business_Card`(`id`,`group_id`,`name`,`job_title`,`company`,`phone`,`email`,`website`,`address`,`image_name`,`date`,`fav`,`note`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Business_Card business_Card) {
                if (business_Card.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, business_Card.getId());
                }
                if (business_Card.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, business_Card.getGroup_id());
                }
                if (business_Card.getName() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, business_Card.getName());
                }
                if (business_Card.getJob_title() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, business_Card.getJob_title());
                }
                if (business_Card.getCompany() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, business_Card.getCompany());
                }
                if (business_Card.getPhone() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, business_Card.getPhone());
                }
                if (business_Card.getEmail() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, business_Card.getEmail());
                }
                if (business_Card.getWebsite() == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, business_Card.getWebsite());
                }
                if (business_Card.getAddress() == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindString(9, business_Card.getAddress());
                }
                if (business_Card.getImage_name() == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindString(10, business_Card.getImage_name());
                }
                if (business_Card.getDate() == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindLong(11, business_Card.getDate().longValue());
                }
                if (business_Card.getFav() == null) {
                    supportSQLiteStatement.bindNull(12);
                } else {
                    supportSQLiteStatement.bindString(12, business_Card.getFav());
                }
                if (business_Card.getNote() == null) {
                    supportSQLiteStatement.bindNull(13);
                } else {
                    supportSQLiteStatement.bindString(13, business_Card.getNote());
                }
            }
        };
        this.__deletionAdapterOfBusiness_Card = new EntityDeletionOrUpdateAdapter<Business_Card>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `Business_Card` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Business_Card business_Card) {
                if (business_Card.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, business_Card.getId());
                }
            }
        };
        this.__updateAdapterOfBusiness_Card = new EntityDeletionOrUpdateAdapter<Business_Card>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `Business_Card` SET `id` = ?,`group_id` = ?,`name` = ?,`job_title` = ?,`company` = ?,`phone` = ?,`email` = ?,`website` = ?,`address` = ?,`image_name` = ?,`date` = ?,`fav` = ?,`note` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Business_Card business_Card) {
                if (business_Card.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, business_Card.getId());
                }
                if (business_Card.getGroup_id() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, business_Card.getGroup_id());
                }
                if (business_Card.getName() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, business_Card.getName());
                }
                if (business_Card.getJob_title() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, business_Card.getJob_title());
                }
                if (business_Card.getCompany() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, business_Card.getCompany());
                }
                if (business_Card.getPhone() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, business_Card.getPhone());
                }
                if (business_Card.getEmail() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, business_Card.getEmail());
                }
                if (business_Card.getWebsite() == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, business_Card.getWebsite());
                }
                if (business_Card.getAddress() == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindString(9, business_Card.getAddress());
                }
                if (business_Card.getImage_name() == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindString(10, business_Card.getImage_name());
                }
                if (business_Card.getDate() == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindLong(11, business_Card.getDate().longValue());
                }
                if (business_Card.getFav() == null) {
                    supportSQLiteStatement.bindNull(12);
                } else {
                    supportSQLiteStatement.bindString(12, business_Card.getFav());
                }
                if (business_Card.getNote() == null) {
                    supportSQLiteStatement.bindNull(13);
                } else {
                    supportSQLiteStatement.bindString(13, business_Card.getNote());
                }
                if (business_Card.getId() == null) {
                    supportSQLiteStatement.bindNull(14);
                } else {
                    supportSQLiteStatement.bindString(14, business_Card.getId());
                }
            }
        };
        this.__preparedStmtOfUpdateGroup = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "UPDATE Business_Card SET group_id = ? where id =?";
            }
        };
        this.__preparedStmtOfRemoveGroup = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "UPDATE Business_Card SET group_id = \"\" where id =?";
            }
        };
        this.__preparedStmtOfSetFav = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "UPDATE Business_Card SET fav = ? where id =?";
            }
        };
    }

    public void insert(Business_Card business_Card) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfBusiness_Card.insert(business_Card);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void delete(Business_Card business_Card) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfBusiness_Card.handle(business_Card);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(Business_Card business_Card) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfBusiness_Card.handle(business_Card);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateGroup(String str, String str2) {
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateGroup.acquire();
        this.__db.beginTransaction();
        if (str2 == null) {
            try {
                acquire.bindNull(1);
            } catch (Throwable th) {
                this.__db.endTransaction();
                this.__preparedStmtOfUpdateGroup.release(acquire);
                throw th;
            }
        } else {
            acquire.bindString(1, str2);
        }
        if (str == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfUpdateGroup.release(acquire);
    }

    public void removeGroup(String str) {
        SupportSQLiteStatement acquire = this.__preparedStmtOfRemoveGroup.acquire();
        this.__db.beginTransaction();
        if (str == null) {
            try {
                acquire.bindNull(1);
            } catch (Throwable th) {
                this.__db.endTransaction();
                this.__preparedStmtOfRemoveGroup.release(acquire);
                throw th;
            }
        } else {
            acquire.bindString(1, str);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfRemoveGroup.release(acquire);
    }

    public void setFav(String str, String str2) {
        SupportSQLiteStatement acquire = this.__preparedStmtOfSetFav.acquire();
        this.__db.beginTransaction();
        if (str2 == null) {
            try {
                acquire.bindNull(1);
            } catch (Throwable th) {
                this.__db.endTransaction();
                this.__preparedStmtOfSetFav.release(acquire);
                throw th;
            }
        } else {
            acquire.bindString(1, str2);
        }
        if (str == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfSetFav.release(acquire);
    }

    public List<Business_Card> getAll_Card() throws Throwable {
        RoomSQLiteQuery roomSQLiteQuery;
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("select * from Business_Card", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("group_id");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(AppMeasurementSdk.ConditionalUserProperty.NAME);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("job_title");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("company");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("phone");
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow("email");
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow("website");
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow("address");
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow("image_name");
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow("date");
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow("fav");
            int columnIndexOrThrow13 = query.getColumnIndexOrThrow("note");
            roomSQLiteQuery = acquire;
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    Business_Card business_Card = new Business_Card();
                    ArrayList arrayList2 = arrayList;
                    business_Card.setId(query.getString(columnIndexOrThrow));
                    business_Card.setGroup_id(query.getString(columnIndexOrThrow2));
                    business_Card.setName(query.getString(columnIndexOrThrow3));
                    business_Card.setJob_title(query.getString(columnIndexOrThrow4));
                    business_Card.setCompany(query.getString(columnIndexOrThrow5));
                    business_Card.setPhone(query.getString(columnIndexOrThrow6));
                    business_Card.setEmail(query.getString(columnIndexOrThrow7));
                    business_Card.setWebsite(query.getString(columnIndexOrThrow8));
                    business_Card.setAddress(query.getString(columnIndexOrThrow9));
                    business_Card.setImage_name(query.getString(columnIndexOrThrow10));
                    if (query.isNull(columnIndexOrThrow11)) {
                        l = null;
                    } else {
                        l = Long.valueOf(query.getLong(columnIndexOrThrow11));
                    }
                    business_Card.setDate(l);
                    business_Card.setFav(query.getString(columnIndexOrThrow12));
                    business_Card.setNote(query.getString(columnIndexOrThrow13));
                    arrayList = arrayList2;
                    arrayList.add(business_Card);
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
//            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th2;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v0, resolved type: com.fittech.bizcardscanner.model.Business_Card} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v1, resolved type: com.fittech.bizcardscanner.model.Business_Card} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v3, resolved type: com.fittech.bizcardscanner.model.Business_Card} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v16, resolved type: java.lang.Long} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v4, resolved type: com.fittech.bizcardscanner.model.Business_Card} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v5, resolved type: com.fittech.bizcardscanner.model.Business_Card} */
    /* JADX WARNING: type inference failed for: r17v2, types: [java.lang.Long] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Business_Card getOnecard(String str) {

//        r18 = this;
//        r0 = r19

        Business_Card business_card = null;
//        java.lang.String r1 = "select * from Business_Card where id = ?";
//        r2 = 1
        androidx.room.RoomSQLiteQuery r1 = androidx.room.RoomSQLiteQuery.acquire("select * from Business_Card where id = ?", 1);
        if (str != null)
        {
//            goto L_0x000f
            r1.bindString(1, str);
        } else {

        r1.bindNull(1);
        }

//        r2 = r18
//        androidx.room.RoomDatabase r0 = this.__db;
        android.database.Cursor r3 = this.__db.query(r1);

        Log.e("onecard", "getOnecard: getCount--> 😋"+r3.getCount() );
        Log.e("onecard", "getOnecard: str--> 😋"+str );
//        java.lang.String r0 = "id"
        int id = r3.getColumnIndexOrThrow("id") ;    // Catch:{ all -> 0x00e7 }
//        java.lang.String r4 = "group_id"
        int group_id = r3.getColumnIndexOrThrow("group_id") ;    // Catch:{ all -> 0x00e7 }
//        java.lang.String r5 = "name"
        int name = r3.getColumnIndexOrThrow("name") ;   // Catch:{ all -> 0x00e7 };
//        java.lang.String r6 = "job_title"
        int jobTitle = r3.getColumnIndexOrThrow("job_title") ;   // Catch:{ all -> 0x00e7 }
//        java.lang.String r7 = "company"
        int company = r3.getColumnIndexOrThrow("company");    // Catch:{ all -> 0x00e7 }
//        java.lang.String r8 = "phone"//
        int phone = r3.getColumnIndexOrThrow("phone") ;   // Catch:{ all -> 0x00e7 }
//        java.lang.String r9 = "email"
        int email = r3.getColumnIndexOrThrow("email") ;   // Catch:{ all -> 0x00e7 }
//        java.lang.String r10 = "website"
        int website = r3.getColumnIndexOrThrow("website");    // Catch:{ all -> 0x00e7 }
//        java.lang.String r11 = "address"
        int address = r3.getColumnIndexOrThrow("address");    // Catch:{ all -> 0x00e7 }
//        java.lang.String r12 = "image_name"
        int image_name = r3.getColumnIndexOrThrow("image_name");    // Catch:{ all -> 0x00e7 }
//        java.lang.String r13 = "date"
        int date = r3.getColumnIndexOrThrow("date") ;   // Catch:{ all -> 0x00e7 }
//        java.lang.String r14 = "fav"
        int r14 = r3.getColumnIndexOrThrow("fav") ;   // Catch:{ all -> 0x00e7 }
//        java.lang.String r15 = "note"
        int r15 = r3.getColumnIndexOrThrow( "note") ;   // Catch:{ all -> 0x00e7 }
        boolean r16 = r3.moveToFirst();

        Log.e("onecard", "getOnecard: getCount--> 😋"+name );
        if (r3.moveToFirst()){

            Business_Card r2 = new Business_Card();     // Catch:{ all -> 0x00e7 }
            r2.setId(r3.getString(id));     // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =      // Catch:{ all -> 0x00e7 }
            r2.setGroup_id(r3.getString(group_id))  ;   // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =     // Catch:{ all -> 0x00e7 }
            r2.setName(r3.getString(name) )  ;   // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =      // Catch:{ all -> 0x00e7 }
            r2.setJob_title(r3.getString(jobTitle))   ;  // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =      // Catch:{ all -> 0x00e7 }
            r2.setCompany(r3.getString(company));     // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =      // Catch:{ all -> 0x00e7 }
            r2.setPhone(r3.getString(phone))   ;  // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =     // Catch:{ all -> 0x00e7 }
            r2.setEmail(r3.getString(email) )  ;   // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =     // Catch:{ all -> 0x00e7 }
            r2.setWebsite(r3.getString(website) ) ;    // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =      // Catch:{ all -> 0x00e7 }
            r2.setAddress(r3.getString(address));     // Catch:{ all -> 0x00e7 }
//            java.lang.String r0 =     // Catch:{ all -> 0x00e7 }
            r2.setImage_name(r3.getString(image_name) );     // Catch:{ all -> 0x00e7 }
            boolean r0 = r3.isNull(date)  ;   // Catch:{ all -> 0x00e7 }
            if (!r0) {
//                goto L_0x00c4

                long r4 = r3.getLong(date) ;    // Catch:{ all -> 0x00e7 }
                java.lang.Long r17 = java.lang.Long.valueOf(r4) ;    // Catch:{ all -> 0x00e7 }
//            goto L_0x00c1

//                r0 = r17
//            goto L_0x00cd

                r2.setDate(r17);     // Catch:{ all -> 0x00e7 }
//                java.lang.String r0 =    // Catch:{ all -> 0x00e7 }
                r2.setFav(r3.getString(r14)  );     // Catch:{ all -> 0x00e7 }
//                java.lang.String r0 =      // Catch:{ all -> 0x00e7 }
                r2.setNote(r3.getString(r15)) ;    // Catch:{ all -> 0x00e7 }
//                r17 = r2

                business_card = r2;
            }
        }

//        L_0x00e0

        r3.close();
        r1.release();
        return business_card;



        /*
            r18 = this;
            r0 = r19
            java.lang.String r1 = "select * from Business_Card where id = ?"
            r2 = 1
            androidx.room.RoomSQLiteQuery r1 = androidx.room.RoomSQLiteQuery.acquire(r1, r2)
            if (r0 != 0) goto L_0x000f
            r1.bindNull(r2)
            goto L_0x0012
        L_0x000f:
            r1.bindString(r2, r0)
        L_0x0012:
            r2 = r18
            androidx.room.RoomDatabase r0 = r2.__db
            android.database.Cursor r3 = r0.query(r1)
            java.lang.String r0 = "id"
            int r0 = r3.getColumnIndexOrThrow(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r4 = "group_id"
            int r4 = r3.getColumnIndexOrThrow(r4)     // Catch:{ all -> 0x00e7 }
            java.lang.String r5 = "name"
            int r5 = r3.getColumnIndexOrThrow(r5)     // Catch:{ all -> 0x00e7 }
            java.lang.String r6 = "job_title"
            int r6 = r3.getColumnIndexOrThrow(r6)     // Catch:{ all -> 0x00e7 }
            java.lang.String r7 = "company"
            int r7 = r3.getColumnIndexOrThrow(r7)     // Catch:{ all -> 0x00e7 }
            java.lang.String r8 = "phone"
            int r8 = r3.getColumnIndexOrThrow(r8)     // Catch:{ all -> 0x00e7 }
            java.lang.String r9 = "email"
            int r9 = r3.getColumnIndexOrThrow(r9)     // Catch:{ all -> 0x00e7 }
            java.lang.String r10 = "website"
            int r10 = r3.getColumnIndexOrThrow(r10)     // Catch:{ all -> 0x00e7 }
            java.lang.String r11 = "address"
            int r11 = r3.getColumnIndexOrThrow(r11)     // Catch:{ all -> 0x00e7 }
            java.lang.String r12 = "image_name"
            int r12 = r3.getColumnIndexOrThrow(r12)     // Catch:{ all -> 0x00e7 }
            java.lang.String r13 = "date"
            int r13 = r3.getColumnIndexOrThrow(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.String r14 = "fav"
            int r14 = r3.getColumnIndexOrThrow(r14)     // Catch:{ all -> 0x00e7 }
            java.lang.String r15 = "note"
            int r15 = r3.getColumnIndexOrThrow(r15)     // Catch:{ all -> 0x00e7 }
            boolean r16 = r3.moveToFirst()     // Catch:{ all -> 0x00e7 }
            r17 = 0
            if (r16 == 0) goto L_0x00e0
            com.fittech.bizcardscanner.model.Business_Card r2 = new com.fittech.bizcardscanner.model.Business_Card     // Catch:{ all -> 0x00e7 }
            r2.<init>()     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r0)     // Catch:{ all -> 0x00e7 }
            r2.setId(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r4)     // Catch:{ all -> 0x00e7 }
            r2.setGroup_id(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r5)     // Catch:{ all -> 0x00e7 }
            r2.setName(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r6)     // Catch:{ all -> 0x00e7 }
            r2.setJob_title(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r7)     // Catch:{ all -> 0x00e7 }
            r2.setCompany(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r8)     // Catch:{ all -> 0x00e7 }
            r2.setPhone(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r9)     // Catch:{ all -> 0x00e7 }
            r2.setEmail(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r10)     // Catch:{ all -> 0x00e7 }
            r2.setWebsite(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r11)     // Catch:{ all -> 0x00e7 }
            r2.setAddress(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r12)     // Catch:{ all -> 0x00e7 }
            r2.setImage_name(r0)     // Catch:{ all -> 0x00e7 }
            boolean r0 = r3.isNull(r13)     // Catch:{ all -> 0x00e7 }
            if (r0 == 0) goto L_0x00c4
        L_0x00c1:
            r0 = r17
            goto L_0x00cd
        L_0x00c4:
            long r4 = r3.getLong(r13)     // Catch:{ all -> 0x00e7 }
            java.lang.Long r17 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x00e7 }
            goto L_0x00c1
        L_0x00cd:
            r2.setDate(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r14)     // Catch:{ all -> 0x00e7 }
            r2.setFav(r0)     // Catch:{ all -> 0x00e7 }
            java.lang.String r0 = r3.getString(r15)     // Catch:{ all -> 0x00e7 }
            r2.setNote(r0)     // Catch:{ all -> 0x00e7 }
            r17 = r2
        L_0x00e0:
            r3.close()
            r1.release()
            return r17
        L_0x00e7:
            r0 = move-exception
            r3.close()
            r1.release()
            throw r0
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.database.dao.Database_Dao_Impl.getOnecard(java.lang.String):com.fittech.bizcardscanner.model.Business_Card");
    }

    public List<Business_Card> getFromGroup(String str) throws Throwable {
        RoomSQLiteQuery roomSQLiteQuery;
        Long l;
        String str2 = str;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("select * from Business_Card where group_id = ?", 1);
        if (str2 == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str2);
        }
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("group_id");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(AppMeasurementSdk.ConditionalUserProperty.NAME);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("job_title");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("company");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("phone");
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow("email");
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow("website");
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow("address");
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow("image_name");
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow("date");
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow("fav");
            int columnIndexOrThrow13 = query.getColumnIndexOrThrow("note");
            roomSQLiteQuery = acquire;
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    Business_Card business_Card = new Business_Card();
                    ArrayList arrayList2 = arrayList;
                    business_Card.setId(query.getString(columnIndexOrThrow));
                    business_Card.setGroup_id(query.getString(columnIndexOrThrow2));
                    business_Card.setName(query.getString(columnIndexOrThrow3));
                    business_Card.setJob_title(query.getString(columnIndexOrThrow4));
                    business_Card.setCompany(query.getString(columnIndexOrThrow5));
                    business_Card.setPhone(query.getString(columnIndexOrThrow6));
                    business_Card.setEmail(query.getString(columnIndexOrThrow7));
                    business_Card.setWebsite(query.getString(columnIndexOrThrow8));
                    business_Card.setAddress(query.getString(columnIndexOrThrow9));
                    business_Card.setImage_name(query.getString(columnIndexOrThrow10));
                    if (query.isNull(columnIndexOrThrow11)) {
                        l = null;
                    } else {
                        l = Long.valueOf(query.getLong(columnIndexOrThrow11));
                    }
                    business_Card.setDate(l);
                    business_Card.setFav(query.getString(columnIndexOrThrow12));
                    business_Card.setNote(query.getString(columnIndexOrThrow13));
                    arrayList = arrayList2;
                    arrayList.add(business_Card);
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
           // th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th2;
        }
    }

    public List<Business_Card> getFavData() throws Throwable {
        RoomSQLiteQuery roomSQLiteQuery;
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("Select * from Business_Card where fav = \"true\"", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("group_id");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(AppMeasurementSdk.ConditionalUserProperty.NAME);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("job_title");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("company");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("phone");
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow("email");
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow("website");
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow("address");
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow("image_name");
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow("date");
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow("fav");
            int columnIndexOrThrow13 = query.getColumnIndexOrThrow("note");
            roomSQLiteQuery = acquire;
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    Business_Card business_Card = new Business_Card();
                    ArrayList arrayList2 = arrayList;
                    business_Card.setId(query.getString(columnIndexOrThrow));
                    business_Card.setGroup_id(query.getString(columnIndexOrThrow2));
                    business_Card.setName(query.getString(columnIndexOrThrow3));
                    business_Card.setJob_title(query.getString(columnIndexOrThrow4));
                    business_Card.setCompany(query.getString(columnIndexOrThrow5));
                    business_Card.setPhone(query.getString(columnIndexOrThrow6));
                    business_Card.setEmail(query.getString(columnIndexOrThrow7));
                    business_Card.setWebsite(query.getString(columnIndexOrThrow8));
                    business_Card.setAddress(query.getString(columnIndexOrThrow9));
                    business_Card.setImage_name(query.getString(columnIndexOrThrow10));
                    if (query.isNull(columnIndexOrThrow11)) {
                        l = null;
                    } else {
                        l = Long.valueOf(query.getLong(columnIndexOrThrow11));
                    }
                    business_Card.setDate(l);
                    business_Card.setFav(query.getString(columnIndexOrThrow12));
                    business_Card.setNote(query.getString(columnIndexOrThrow13));
                    arrayList = arrayList2;
                    arrayList.add(business_Card);
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
//            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th2;
        }
    }
}
