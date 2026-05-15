# GWH_TM_WHS
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
| 18 | WHS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WHS_STRT_YMD | DATE | Y | Warehouse Business Start Date / 倉庫_業務開始日 |
| 21 | WHS_END_YMD | DATE | Y | Warehouse Business End Date / 倉庫_業務終了日 |
| 22 | WHS_NAM1 | NVARCHAR2(100) | Y | Warehouse Name1 / 倉庫名称1st |
| 23 | WHS_NAM2 | NVARCHAR2(100) | Y | Warehouse Name2 / 倉庫名称2nd |
| 24 | WHS_ZIP | NVARCHAR2(20) | Y | Warehouse Zip / 倉庫郵便番号 |
| 25 | WHS_JIS | NVARCHAR2(22) | Y | Warehouse Jis / 倉庫住所コード |
| 26 | WHS_ADR1 | NVARCHAR2(100) | Y | Warehouse Address1 / 倉庫住所1 |
| 27 | WHS_ADR2 | NVARCHAR2(100) | Y | Warehouse Address2 / 倉庫住所2 |
| 28 | WHS_ADR3 | NVARCHAR2(100) | Y | Warehouse Address3 / 倉庫住所3 |
| 29 | WHS_ADR4 | NVARCHAR2(100) | Y | Warehouse Address4 / 倉庫住所4 |
| 30 | WHS_ADR5 | NVARCHAR2(100) | Y | Warehouse Address5 / 倉庫住所5 |
| 31 | WHS_TEL | NVARCHAR2(40) | Y | Warehouse Tel / 倉庫電話番号 |
| 32 | WHS_CTRY_COD | CHAR(2) | Y | Country Code / 国コード |
| 33 | WHS_CITY_COD | NVARCHAR2(6) | Y | City Code / 都市コード |
| 34 | WHS_STAT_COD | NVARCHAR2(4) | Y | State Code / 州コード |
| 35 | WHS_STR_COD | NVARCHAR2(12) | Y | Store Code / 店所コード |
| 36 | WHS_ABYT | NUMBER(1) | Y | Area Code Use Byte Contorol / エリアコード使用バイト数制御 |
| 37 | WHS_RBP1 | NUMBER(1) | Y | Rack Breakpoint1 / ラック区切り位置１ |
| 38 | WHS_RBP2 | NUMBER(1) | Y | Rack Breakpoint2 / ラック区切り位置２ |
| 39 | WHS_RBP3 | NUMBER(1) | Y | Rack Breakpoint3 / ラック区切り位置３ |
| 40 | WHS_RBP4 | NUMBER(1) | Y | Rack Breakpoint4 / ラック区切り位置４ |
| 41 | WHS_CITY_NAM | NVARCHAR2(100) | Y | City Name / 都市名 |
| 42 | WHS_X_POS | NUMBER(8,5) | Y | latitude / 緯度 |
| 43 | WHS_Y_POS | NUMBER(8,5) | Y | longitude / 経度 |
| 44 | WHS_PHYS_COD | NVARCHAR2(8) | Y | Physical Warehouse Code / 物理倉庫コード |
