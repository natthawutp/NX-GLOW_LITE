# GWH_TM_WCH
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
| 12 | UPD_TMID | CHAR(100) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | CHAR(50) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | CHAR(30) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | CHAR(3) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | WCH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WCH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WCH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WCH_CHTP_TYP | CHAR(1) | N | Charge Type / Charge Type |
| 22 | WCH_CHCD_COD | CHAR(3) | N | Charge Code / Charge Code |
| 23 | WCH_CHNM_NAM | NVARCHAR2(70) | Y | Charge Description / Charge Description |
| 24 | WCH_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 25 | WCH_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 26 | WCH_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 27 | WCH_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 28 | WCH_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 29 | WCH_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 30 | WCH_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 31 | WCH_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 32 | WCH_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 33 | WCH_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 34 | WCH_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 35 | WCH_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 36 | WCH_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 37 | WCH_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 38 | WCH_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 39 | WCH_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 40 | WCH_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 41 | WCH_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 42 | WCH_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 43 | WCH_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 44 | WCH_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 45 | WCH_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 46 | WCH_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 47 | WCH_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 48 | WCH_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 49 | WCH_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 50 | WCH_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 51 | WCH_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 52 | WCH_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 53 | WCH_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
