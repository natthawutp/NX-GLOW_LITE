# GWH_TJ_WSPR
#Entity #Standard #WAREHOUSE_CHARGE

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
| 18 | WSPR_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WSPR_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WSPR_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WSPR_SP_NUM | NVARCHAR2(12) | N | Order No. / 出荷番号 |
| 22 | WSPR_CARC_COD | NVARCHAR2(10) | Y | Carrier Code / Carrier Code |
| 23 | WSPR_PKDT_YMD | DATE | Y | Picking Date / Picking Date |
| 24 | WSPR_SHDT_YMD | DATE | Y | Actual Shipping Date / Actual Shipping Date |
| 25 | WSPR_BLDT_YMD | DATE | Y | Billing Statement Date / Billing Statement Date |
| 26 | WSPR_PRO_NUM | NVARCHAR2(50) | Y | PRO# / PRO# |
| 27 | WSPR_FRCH | NUMBER(14,4) | Y | Freight Charge / Freight Charge |
| 28 | WSPR_RPNO | NUMBER(5) | Y | No. of Repacks / No. of Repacks |
| 29 | WSPR_CTNO | NUMBER(5) | Y | No. of Cartons / No. of Cartons |
| 30 | WSPR_PLNO | NUMBER(5) | Y | No. of Pallets / No. of Pallets |
| 31 | WSPR_LBNO | NUMBER(5) | Y | No. of Labels / No. of Labels |
| 32 | WSPR_PTNO | NUMBER(5) | Y | No. of Palletized / No. of Palletized |
| 33 | WSPR_RBNO | NUMBER(5) | Y | No. of Reboxed / No. of Reboxed |
| 34 | WSPR_SPINS1 | NVARCHAR2(140) | Y | Shipping Instaction1 / Shipping Instaction1 |
| 35 | WSPR_SPINS2 | NVARCHAR2(140) | Y | Shipping Instaction2 / Shipping Instaction2 |
| 36 | WSPR_SPINS3 | NVARCHAR2(140) | Y | Shipping Instaction3 / Shipping Instaction3 |
| 37 | WSPR_SPINS4 | NVARCHAR2(140) | Y | Shipping Instaction4 / Shipping Instaction4 |
| 38 | WSPR_SPINS5 | NVARCHAR2(140) | Y | Shipping Instaction5 / Shipping Instaction5 |
| 39 | WSPR_NO1 | NUMBER(5) | Y | Filler No. 1 / Filler No. 1 |
| 40 | WSPR_NO2 | NUMBER(5) | Y | Filler No. 2 / Filler No. 2 |
| 41 | WSPR_NO3 | NUMBER(5) | Y | Filler No. 3 / Filler No. 3 |
| 42 | WSPR_NO4 | NUMBER(5) | Y | Filler No. 4 / Filler No. 4 |
| 43 | WSPR_NO5 | NUMBER(5) | Y | Filler No. 5 / Filler No. 5 |
| 44 | WSPR_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 45 | WSPR_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 46 | WSPR_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 47 | WSPR_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 48 | WSPR_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 49 | WSPR_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 50 | WSPR_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 51 | WSPR_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 52 | WSPR_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 53 | WSPR_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 54 | WSPR_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 55 | WSPR_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 56 | WSPR_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 57 | WSPR_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 58 | WSPR_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 59 | WSPR_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 60 | WSPR_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 61 | WSPR_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 62 | WSPR_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 63 | WSPR_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 64 | WSPR_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 65 | WSPR_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 66 | WSPR_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 67 | WSPR_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 68 | WSPR_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 69 | WSPR_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 70 | WSPR_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 71 | WSPR_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 72 | WSPR_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 73 | WSPR_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
