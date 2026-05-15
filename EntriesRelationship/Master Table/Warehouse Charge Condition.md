# GWH_TM_WCN
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
| 18 | WCN_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WCN_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WCN_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WCN_TBL_TYP | NVARCHAR2(20) | N | Table type / テーブルタイプ |
| 22 | WCN_UNQ_COD | NVARCHAR2(20) | N | Unique Code for the Number  / ユニークコード |
| 23 | WCN_NM20_NAM | NVARCHAR2(40) | Y | 20 byte constant field / 20 byte constant field |
| 24 | WCN_NM8A_NAM | NVARCHAR2(160) | N | 80 byte constant field　(Description) / 80 byte constant field　(Description) |
| 25 | WCN_NM8B_NAM | NVARCHAR2(160) | Y | 80 byte constant field / 80 byte constant field |
| 26 | WCN_NM8C_NAM | NVARCHAR2(160) | Y | 80 byte constant field / 80 byte constant field |
| 27 | WCN_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 28 | WCN_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 29 | WCN_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 30 | WCN_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 31 | WCN_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 32 | WCN_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 33 | WCN_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 34 | WCN_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 35 | WCN_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 36 | WCN_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 37 | WCN_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 38 | WCN_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 39 | WCN_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 40 | WCN_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 41 | WCN_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 42 | WCN_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 43 | WCN_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 44 | WCN_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 45 | WCN_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 46 | WCN_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 47 | WCN_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 48 | WCN_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 49 | WCN_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 50 | WCN_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 51 | WCN_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 52 | WCN_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 53 | WCN_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 54 | WCN_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 55 | WCN_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 56 | WCN_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
