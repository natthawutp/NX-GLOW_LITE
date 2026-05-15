# GWH_TM_UDD
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
| 18 | UDD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | UDD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | UDD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | PHYSICAL_TBL_NM | NVARCHAR2(100) | N | Physical Table Name / 物理テーブル名 |
| 22 | DEL_APL_FLG | NVARCHAR2(4) | N | Delete Apply Flag / 削除適用フラグ |
| 23 | DEL_ORDER | NUMBER(4) | Y | Delete Order / 削除順 |
| 24 | RET_DAYS | NUMBER(4) | Y | Retention days. / 保持日数 |
| 25 | DEL_KEY_RET_DAYS | NVARCHAR2(60) | Y | Delete Key ItemName(Retention days). / 削除条件キーカラム名（保持日数） |
| 26 | OTH_DEL_CDT | NVARCHAR2(4000) | Y | Other Delete conditions. / その他削除条件 |
