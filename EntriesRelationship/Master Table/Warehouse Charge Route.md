# GWH_TM_WRC
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
| 18 | WRC_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WRC_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WRC_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WRC_CHTP_TYP | CHAR(1) | N | Charge Type / Charge Type |
| 22 | WRC_RTCD_COD | CHAR(2) | Y | Rate Code / Rate Code |
| 23 | WRC_RTNM_NAM | NVARCHAR2(70) | Y | Rate Description / Rate Description |
| 24 | WRC_PRFG_FLG | CHAR(1) | Y | By Product Flag / By Product Flag |
| 25 | WRC_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 26 | WRC_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 27 | WRC_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 28 | WRC_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 29 | WRC_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 30 | WRC_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 31 | WRC_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 32 | WRC_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 33 | WRC_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 34 | WRC_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 35 | WRC_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 36 | WRC_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 37 | WRC_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 38 | WRC_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 39 | WRC_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 40 | WRC_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 41 | WRC_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 42 | WRC_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 43 | WRC_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 44 | WRC_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 45 | WRC_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 46 | WRC_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 47 | WRC_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 48 | WRC_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 49 | WRC_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 50 | WRC_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 51 | WRC_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 52 | WRC_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 53 | WRC_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 54 | WRC_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
