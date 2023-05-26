package online.dailyq.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import online.dailyq.db.Converters;
import online.dailyq.db.entity.QuestionEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class QuestionDao_Impl implements QuestionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuestionEntity> __insertionAdapterOfQuestionEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public QuestionDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuestionEntity = new EntityInsertionAdapter<QuestionEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `question` (`id`,`text`,`answerCount`,`updatedAt`,`createdAt`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, QuestionEntity value) {
        final String _tmp = __converters.toString(value.getId());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getText() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getText());
        }
        stmt.bindLong(3, value.getAnswerCount());
        final Long _tmp_1 = __converters.toLong(value.getUpdatedAt());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp_1);
        }
        final Long _tmp_2 = __converters.toLong(value.getCreatedAt());
        if (_tmp_2 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_2);
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM question";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrReplace(final QuestionEntity[] questions,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestionEntity.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertOrReplace(final List<QuestionEntity> questions,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestionEntity.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> arg0) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, arg0);
  }

  @Override
  public Object get(final String fromDate, final Continuation<? super QuestionEntity> arg1) {
    final String _sql = "SELECT * FROM question WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (fromDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fromDate);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<QuestionEntity>() {
      @Override
      public QuestionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfAnswerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "answerCount");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final QuestionEntity _result;
          if(_cursor.moveToFirst()) {
            final LocalDate _tmpId;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfId);
            }
            _tmpId = __converters.toLocalDate(_tmp);
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final int _tmpAnswerCount;
            _tmpAnswerCount = _cursor.getInt(_cursorIndexOfAnswerCount);
            final Date _tmpUpdatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.toDate(_tmp_1);
            final Date _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.toDate(_tmp_2);
            _result = new QuestionEntity(_tmpId,_tmpText,_tmpAnswerCount,_tmpUpdatedAt,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public PagingSource<Integer, QuestionEntity> getPagingSource() {
    final String _sql = "SELECT * FROM question ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new LimitOffsetPagingSource<QuestionEntity>(_statement, __db, "question") {
      @Override
      protected List<QuestionEntity> convertRows(Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(cursor, "text");
        final int _cursorIndexOfAnswerCount = CursorUtil.getColumnIndexOrThrow(cursor, "answerCount");
        final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "updatedAt");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "createdAt");
        final List<QuestionEntity> _result = new ArrayList<QuestionEntity>(cursor.getCount());
        while(cursor.moveToNext()) {
          final QuestionEntity _item;
          final LocalDate _tmpId;
          final String _tmp;
          if (cursor.isNull(_cursorIndexOfId)) {
            _tmp = null;
          } else {
            _tmp = cursor.getString(_cursorIndexOfId);
          }
          _tmpId = __converters.toLocalDate(_tmp);
          final String _tmpText;
          if (cursor.isNull(_cursorIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = cursor.getString(_cursorIndexOfText);
          }
          final int _tmpAnswerCount;
          _tmpAnswerCount = cursor.getInt(_cursorIndexOfAnswerCount);
          final Date _tmpUpdatedAt;
          final Long _tmp_1;
          if (cursor.isNull(_cursorIndexOfUpdatedAt)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = cursor.getLong(_cursorIndexOfUpdatedAt);
          }
          _tmpUpdatedAt = __converters.toDate(_tmp_1);
          final Date _tmpCreatedAt;
          final Long _tmp_2;
          if (cursor.isNull(_cursorIndexOfCreatedAt)) {
            _tmp_2 = null;
          } else {
            _tmp_2 = cursor.getLong(_cursorIndexOfCreatedAt);
          }
          _tmpCreatedAt = __converters.toDate(_tmp_2);
          _item = new QuestionEntity(_tmpId,_tmpText,_tmpAnswerCount,_tmpUpdatedAt,_tmpCreatedAt);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
