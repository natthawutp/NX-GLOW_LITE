# GWH_TM_CODE
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
| 18 | CODE_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | CODE_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | CODE_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | CODE_KND | NVARCHAR2(20) | N | Kind / 種別 |
| 22 | CODE_COD | NVARCHAR2(40) | N | Code / コード |
| 23 | CODE_LBL_COD | NVARCHAR2(200) | Y | Label Code / ラベルコード |
| 24 | CODE_INFO1 | NVARCHAR2(100) | Y | Information1 / 情報1 |
| 25 | CODE_INFO2 | NVARCHAR2(100) | Y | Information2 / 情報2 |
| 26 | CODE_INFO3 | NVARCHAR2(100) | Y | Information3 / 情報3 |
| 27 | CODE_NUM1 | NUMBER(15,5) | Y | Number1 / 数値1 |
| 28 | CODE_NUM2 | NUMBER(15,5) | Y | Number2 / 数値2 |
| 29 | CODE_NUM3 | NUMBER(15,5) | Y | Number3 / 数値3 |
| 30 | CODE_RMKS | NVARCHAR2(100) | Y | Remarks / 備考 |
| 31 | CODE_INFO4 | NVARCHAR2(1000) | Y | Information4 / 情報4 |
| 32 | CODE_INFO5 | NVARCHAR2(1000) | Y | Information5 / 情報5 |
| 33 | CODE_STD_FLG | CHAR(1) | N | Standard Function Flag / 標準機能フラグ |
