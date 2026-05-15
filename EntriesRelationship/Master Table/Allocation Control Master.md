# GWH_TM_ALCO
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
| 18 | ALCO_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | ALCO_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | ALCO_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | ALCO_PROD_COD | NVARCHAR2(100) | Y | Product Code / 商品コード |
| 22 | ALCO_ALP_KEY1 | NVARCHAR2(100) | N | Allocation Priority Key1 / 優先キー1 |
| 23 | ALCO_ALP_KEY2 | NVARCHAR2(100) | Y | Allocation Priority Key2 / 優先キー2 |
| 24 | ALCO_ALP_KEY3 | NVARCHAR2(100) | Y | Allocation Priority Key3 / 優先キー3 |
| 25 | ALCO_ALP_KEY4 | NVARCHAR2(100) | Y | Allocation Priority Key4 / 優先キー4 |
| 26 | ALCO_ALP_KEY5 | NVARCHAR2(100) | Y | Allocation Priority Key5 / 優先キー5 |
| 27 | ALCO_ALP_KEY6 | NVARCHAR2(100) | Y | Allocation Priority Key6 / 優先キー6 |
| 28 | ALCO_ALP_KEY7 | NVARCHAR2(100) | Y | Allocation Priority Key7 / 優先キー7 |
| 29 | ALCO_ALP_KEY8 | NVARCHAR2(100) | Y | Allocation Priority Key8 / 優先キー8 |
| 30 | ALCO_ALP_KEY9 | NVARCHAR2(100) | Y | Allocation Priority Key9 / 優先キー9 |
| 31 | ALCO_ALP_KEY10 | NVARCHAR2(100) | Y | Allocation Priority Key10 / 優先キー10 |
| 32 | ALCO_ALP_KEY11 | NVARCHAR2(100) | Y | Allocation Priority Key11 / 優先キー11 |
| 33 | ALCO_ALP_KEY12 | NVARCHAR2(100) | Y | Allocation Priority Key12 / 優先キー12 |
| 34 | ALCO_ALP_KEY13 | NVARCHAR2(100) | Y | Allocation Priority Key13 / 優先キー13 |
| 35 | ALCO_ALP_KEY14 | NVARCHAR2(100) | Y | Allocation Priority Key14 / 優先キー14 |
| 36 | ALCO_ALP_KEY15 | NVARCHAR2(100) | Y | Allocation Priority Key15 / 優先キー15 |
| 37 | ALCO_PRDG_COD | NVARCHAR2(30) | Y | Product Group Code/商品グループコード |
