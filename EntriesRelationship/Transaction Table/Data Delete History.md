# GWH_TJ_DD_HIS
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
| 18 | DDH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DDH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DDH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DDH_PTBL_NAM | NVARCHAR2(100) | N | Physical Table Name / 物理テーブル名 |
| 22 | DDH_STR_YMDHMS | TIMESTAMP(6) | N | Start Timestamp / 開始タイムスタンプ |
| 23 | DDH_END_YMDHMS | TIMESTAMP(6) | N | End Timestamp / 終了タイムスタンプ |
| 24 | DDH_EXE_RSLT | CHAR(2) | N | Execution Result / 実行結果 |
| 25 | DDH_DEL_CNT | NUMBER(15) | Y | Delete Count / 削除件数 |
| 26 | DDH_EXE_SQL | NVARCHAR2(4000) | N | Execute SQL / 実行SQL |
| 27 | DDH_ERR_MSG | NVARCHAR2(4000) | Y | Error Message / エラーメッセージ |
