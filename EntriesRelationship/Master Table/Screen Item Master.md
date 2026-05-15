# GWH_TM_SRIT
#Entity #Standard #MASTER

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
| 18 | SRIT_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SRIT_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SRIT_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SRIT_SC_COD | NVARCHAR2(40) | N | Screen Id / 画面ID |
| 22 | SRIT_CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 23 | SRIT_ITEM1 | NVARCHAR2(100) | Y | Search Item1 / 検索項目1 |
| 24 | SRIT_ITEM2 | NVARCHAR2(100) | Y | Search Item2 / 検索項目2 |
| 25 | SRIT_ITEM3 | NVARCHAR2(100) | Y | Search Item3 / 検索項目3 |
| 26 | SRIT_ITEM4 | NVARCHAR2(100) | Y | Search Item4 / 検索項目4 |
| 27 | SRIT_ITEM5 | NVARCHAR2(100) | Y | Search Item5 / 検索項目5 |
| 28 | SRIT_ITEM6 | NVARCHAR2(100) | Y | Search Item6 / 検索項目6 |
| 29 | SRIT_ITEM7 | NVARCHAR2(100) | Y | Search Item7 / 検索項目7 |
| 30 | SRIT_ITEM8 | NVARCHAR2(100) | Y | Search Item8 / 検索項目8 |
| 31 | SRIT_ITEM9 | NVARCHAR2(100) | Y | Search Item9 / 検索項目9 |
| 32 | SRIT_ITEM10 | NVARCHAR2(100) | Y | Search Item10 / 検索項目10 |
| 33 | SRIT_ITEM11 | NVARCHAR2(100) | Y | Search Item11 / 検索項目11 |
| 34 | SRIT_ITEM12 | NVARCHAR2(100) | Y | Search Item12 / 検索項目12 |
| 35 | SRIT_ITEM13 | NVARCHAR2(100) | Y | Search Item13 / 検索項目13 |
| 36 | SRIT_ITEM14 | NVARCHAR2(100) | Y | Search Item14 / 検索項目14 |
| 37 | SRIT_ITEM15 | NVARCHAR2(100) | Y | Search Item15 / 検索項目15 |
| 38 | SRIT_ITEM16 | NVARCHAR2(100) | Y | Search Item16 / 検索項目16 |
| 39 | SRIT_ITEM17 | NVARCHAR2(100) | Y | Search Item17 / 検索項目17 |
| 40 | SRIT_ITEM18 | NVARCHAR2(100) | Y | Search Item18 / 検索項目18 |
| 41 | SRIT_ITEM19 | NVARCHAR2(100) | Y | Search Item19 / 検索項目19 |
| 42 | SRIT_ITEM20 | NVARCHAR2(100) | Y | Search Item20 / 検索項目20 |
