# GWH_TJ_WSSP
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
| 18 | WSSP_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WSSP_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WSSP_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WSSP_SP_NUM | NVARCHAR2(12) | N | Order No. / 出荷番号 |
| 22 | WSSP_CARC_COD | NVARCHAR2(10) | Y | Carrier Code / Carrier Code |
| 23 | WSSP_SHDT_YMD | DATE | Y | Actual Shipping Date / Actual Shipping Date |
| 24 | WSSP_PRO_NUM | NVARCHAR2(50) | Y | PRO# / PRO# |
| 25 | WSSP_FRCH | NUMBER(14,4) | Y | Freight Charge / Freight Charge |
| 26 | WSSP_RPNO | NUMBER(5) | Y | No. of Repacks / No. of Repacks |
| 27 | WSSP_CTNO | NUMBER(5) | Y | No. of Cartons / No. of Cartons |
| 28 | WSSP_PLNO | NUMBER(5) | Y | No. of Pallets / No. of Pallets |
| 29 | WSSP_LBNO | NUMBER(5) | Y | No. of Labels / No. of Labels |
| 30 | WSSP_PTNO | NUMBER(5) | Y | No. of Palletized / No. of Palletized |
| 31 | WSSP_RBNO | NUMBER(5) | Y | No. of Reboxed / No. of Reboxed |
| 32 | WSSP_SPINS1 | NVARCHAR2(140) | Y | Shipping Instaction1 / Shipping Instaction1 |
| 33 | WSSP_SPINS2 | NVARCHAR2(140) | Y | Shipping Instaction2 / Shipping Instaction2 |
| 34 | WSSP_SPINS3 | NVARCHAR2(140) | Y | Shipping Instaction3 / Shipping Instaction3 |
| 35 | WSSP_SPINS4 | NVARCHAR2(140) | Y | Shipping Instaction4 / Shipping Instaction4 |
| 36 | WSSP_SPINS5 | NVARCHAR2(140) | Y | Shipping Instaction5 / Shipping Instaction5 |
| 37 | WSSP_NO1 | NUMBER(5) | Y | Filler No. 1 / Filler No. 1 |
| 38 | WSSP_NO2 | NUMBER(5) | Y | Filler No. 2 / Filler No. 2 |
| 39 | WSSP_NO3 | NUMBER(5) | Y | Filler No. 3 / Filler No. 3 |
| 40 | WSSP_NO4 | NUMBER(5) | Y | Filler No. 4 / Filler No. 4 |
| 41 | WSSP_NO5 | NUMBER(5) | Y | Filler No. 5 / Filler No. 5 |
| 42 | WSSP_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 43 | WSSP_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 44 | WSSP_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 45 | WSSP_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 46 | WSSP_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 47 | WSSP_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 48 | WSSP_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 49 | WSSP_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 50 | WSSP_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 51 | WSSP_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 52 | WSSP_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 53 | WSSP_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 54 | WSSP_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 55 | WSSP_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 56 | WSSP_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 57 | WSSP_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 58 | WSSP_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 59 | WSSP_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 60 | WSSP_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 61 | WSSP_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 62 | WSSP_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 63 | WSSP_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 64 | WSSP_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 65 | WSSP_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 66 | WSSP_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 67 | WSSP_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 68 | WSSP_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 69 | WSSP_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 70 | WSSP_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 71 | WSSP_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
