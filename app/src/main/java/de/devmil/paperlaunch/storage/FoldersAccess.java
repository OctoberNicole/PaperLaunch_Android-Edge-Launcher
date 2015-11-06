package de.devmil.paperlaunch.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.devmil.paperlaunch.utils.BitmapUtils;

public class FoldersAccess {
    private static final String[] foldersColumns = new String[]
            {
                    EntriesSQLiteOpenHelper.COLUMN_ID,
                    EntriesSQLiteOpenHelper.COLUMN_FOLDERS_NAME,
                    EntriesSQLiteOpenHelper.COLUMN_FOLDERS_ICON
            };
    private static final int INDEX_COLUMN_ID = 0;
    private static final int INDEX_COLUMN_NAME = 1;
    private static final int INDEX_COLUMN_ICON = 2;

    private SQLiteDatabase mDatabase;

    public FoldersAccess(SQLiteDatabase database) {
        mDatabase = database;
    }

    public FolderDTO queryFolder(long folderId) {
        Cursor c = mDatabase.query(
                EntriesSQLiteOpenHelper.TABLE_FOLDERS,
                foldersColumns,
                EntriesSQLiteOpenHelper.COLUMN_ID + " = " + folderId,
                null,
                null,
                null,
                null
        );

        if(c.moveToFirst()) {
            return cursorToFolder(c);
        }
        return null;
    }

    public FolderDTO createNew() {
        long id = mDatabase.insert(
                EntriesSQLiteOpenHelper.TABLE_FOLDERS,
                null,
                null);

        return queryFolder(id);
    }

    public void update(FolderDTO folder) {
        ContentValues values = new ContentValues();

        folderToValues(folder, values);

        mDatabase.update(
                EntriesSQLiteOpenHelper.TABLE_FOLDERS,
                values,
                EntriesSQLiteOpenHelper.COLUMN_ID + " = " + folder.getId(),
                null
        );
    }

    public void delete(FolderDTO folder) {
        mDatabase.delete(
                EntriesSQLiteOpenHelper.TABLE_FOLDERS,
                EntriesSQLiteOpenHelper.COLUMN_ID + " = " + folder.getId(),
                null
        );
    }

    private FolderDTO cursorToFolder(Cursor cursor) {
        return new FolderDTO(
                cursor.getInt(INDEX_COLUMN_ID),
                cursor.getString(INDEX_COLUMN_NAME),
                BitmapUtils.getIcon(cursor.getBlob(INDEX_COLUMN_ICON))
        );
    }

    private void folderToValues(FolderDTO folder, ContentValues values) {
        values.put(EntriesSQLiteOpenHelper.COLUMN_FOLDERS_NAME, folder.getName());
        values.put(EntriesSQLiteOpenHelper.COLUMN_FOLDERS_ICON, BitmapUtils.getBytes(folder.getIcon()));
    }
}
