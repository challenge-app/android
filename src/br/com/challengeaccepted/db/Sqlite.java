package br.com.challengeaccepted.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Sqlite  extends SQLiteOpenHelper  {

	private static String BANCO_NOME = "banco.db";
	private static int BANCO_VERSAO = 1;
	
	// Construtor
	public Sqlite(Context context) {
		super(context, BANCO_NOME, null, BANCO_VERSAO);
		
	}
	
	//Executa apenas quando nao existe o arquivo banco.db, ou seja, apenas quando esta criando o banco de dados.
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//Tabela DispoDb
		db.execSQL(" CREATE TABLE IF NOT EXISTS FRIENDS( USER_EMAIL text, " +
				                                      " FRIEND_EMAIL text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
