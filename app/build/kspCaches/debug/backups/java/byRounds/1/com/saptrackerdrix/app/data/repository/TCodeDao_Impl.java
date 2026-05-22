package com.saptrackerdrix.app.data.repository;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.saptrackerdrix.app.data.model.TCode;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TCodeDao_Impl implements TCodeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TCode> __insertionAdapterOfTCode;

  private final EntityDeletionOrUpdateAdapter<TCode> __deletionAdapterOfTCode;

  private final EntityDeletionOrUpdateAdapter<TCode> __updateAdapterOfTCode;

  public TCodeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTCode = new EntityInsertionAdapter<TCode>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tcodes` (`id`,`code`,`purpose`,`module`,`notes`,`createdAt`,`favorite`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TCode entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCode());
        statement.bindString(3, entity.getPurpose());
        statement.bindString(4, entity.getModule());
        statement.bindString(5, entity.getNotes());
        statement.bindLong(6, entity.getCreatedAt());
        final int _tmp = entity.getFavorite() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__deletionAdapterOfTCode = new EntityDeletionOrUpdateAdapter<TCode>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tcodes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TCode entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfTCode = new EntityDeletionOrUpdateAdapter<TCode>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tcodes` SET `id` = ?,`code` = ?,`purpose` = ?,`module` = ?,`notes` = ?,`createdAt` = ?,`favorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TCode entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCode());
        statement.bindString(3, entity.getPurpose());
        statement.bindString(4, entity.getModule());
        statement.bindString(5, entity.getNotes());
        statement.bindLong(6, entity.getCreatedAt());
        final int _tmp = entity.getFavorite() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindString(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertTCode(final TCode tCode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTCode.insert(tCode);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTCode(final TCode tCode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTCode.handle(tCode);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTCode(final TCode tCode, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTCode.handle(tCode);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TCode>> getAllTCodes() {
    final String _sql = "SELECT * FROM tcodes ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tcodes"}, new Callable<List<TCode>>() {
      @Override
      @NonNull
      public List<TCode> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfPurpose = CursorUtil.getColumnIndexOrThrow(_cursor, "purpose");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "module");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<TCode> _result = new ArrayList<TCode>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TCode _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpPurpose;
            _tmpPurpose = _cursor.getString(_cursorIndexOfPurpose);
            final String _tmpModule;
            _tmpModule = _cursor.getString(_cursorIndexOfModule);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new TCode(_tmpId,_tmpCode,_tmpPurpose,_tmpModule,_tmpNotes,_tmpCreatedAt,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TCode>> searchTCodes(final String query) {
    final String _sql = "SELECT * FROM tcodes WHERE code LIKE '%' || ? || '%' OR purpose LIKE '%' || ? || '%' OR module LIKE '%' || ? || '%' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tcodes"}, new Callable<List<TCode>>() {
      @Override
      @NonNull
      public List<TCode> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfPurpose = CursorUtil.getColumnIndexOrThrow(_cursor, "purpose");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "module");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<TCode> _result = new ArrayList<TCode>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TCode _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpPurpose;
            _tmpPurpose = _cursor.getString(_cursorIndexOfPurpose);
            final String _tmpModule;
            _tmpModule = _cursor.getString(_cursorIndexOfModule);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new TCode(_tmpId,_tmpCode,_tmpPurpose,_tmpModule,_tmpNotes,_tmpCreatedAt,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TCode>> getFavorites() {
    final String _sql = "SELECT * FROM tcodes WHERE favorite = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tcodes"}, new Callable<List<TCode>>() {
      @Override
      @NonNull
      public List<TCode> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfPurpose = CursorUtil.getColumnIndexOrThrow(_cursor, "purpose");
          final int _cursorIndexOfModule = CursorUtil.getColumnIndexOrThrow(_cursor, "module");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<TCode> _result = new ArrayList<TCode>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TCode _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpPurpose;
            _tmpPurpose = _cursor.getString(_cursorIndexOfPurpose);
            final String _tmpModule;
            _tmpModule = _cursor.getString(_cursorIndexOfModule);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new TCode(_tmpId,_tmpCode,_tmpPurpose,_tmpModule,_tmpNotes,_tmpCreatedAt,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
