# GWH_TJ_MHIS
#Entity #Standard #HISTORY

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | MHS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | MHS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | MHS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | MHS_ZIP_STR_KND | NVARCHAR2(4) | N | Zip Store Pattern / 郵便店所パターン |
| 22 | MHS_SEQ | NUMBER(15,5) | N | Execution SEQ / 実行SEQ |
| 23 | MHS_USR_ID | NVARCHAR2(30) | Y | User ID / ユーザID |
| 24 | MHS_EXE_URL | NVARCHAR2(2000) | Y | Execution URL / 実行URL |
| 25 | MHS_REH_TOKN | NVARCHAR2(900) | Y | refresh　token / リフレッシュトークン |
| 26 | MHS_ACE_TOKN | NVARCHAR2(900) | Y | access token / アクセストークン |
| 27 | MHS_API_REQ1 | NVARCHAR2(4000) | Y | API request1 / APIリクエスト1 |
| 28 | MHS_API_REQ2 | NVARCHAR2(4000) | Y | API request2 / APIリクエスト2 |
| 29 | MHS_API_RES1 | NVARCHAR2(4000) | Y | API response1 / APIレスポンス1 |
| 30 | MHS_API_RES2 | NVARCHAR2(4000) | Y | API response2 / APIレスポンス2 |
| 31 | MHS_DLCT_NUM | CHAR(14) | Y | Delivery Control Number / 配送管理番号 |
| 32 | MHS_RES_STS | NVARCHAR2(6) | Y | Response Status / レスポンスステータス |
| 33 | MHS_MSG_COD | NVARCHAR2(20) | Y | Message Id / メッセージID |
