# GWH_TM_HNST
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(40) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(40) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | HNST_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | HNST_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | HNST_COD | NVARCHAR2(8) | N | Handling Store Code / 取扱店コード |
| 21 | HNST_NAM | NVARCHAR2(40) | N | Handling Store Name / 取扱店名称 |
| 22 | HNST_TEL | NVARCHAR2(40) | N | Handling Store TEL / 取扱店電話番号 |
| 23 | HNST_SRT_COD | NVARCHAR2(6) | N | Departure Sorting Code / 発仕分コード |
| 24 | HNST_DRVR_NUM | NVARCHAR2(12) | N | Handling Store Driver Number / 集荷ドライバー番号 |
| 25 | HNST_SUM_ST_COD | NVARCHAR2(8) | N | Summary Store Code / 発集計店所コード |
| 26 | HNST_ACC_SC_COD | NVARCHAR2(12) | N | Account Section Code / 発経理課所コード |
| 27 | HNST_ACCR_ST_COD | NVARCHAR2(8) | N | Accounts Receivable Store Code / 売掛集配店所コード |
| 28 | HNST_ACCR_SC_COD | NVARCHAR2(12) | N | Accounts Receivable Section Code / 売掛金経理課所コード |
