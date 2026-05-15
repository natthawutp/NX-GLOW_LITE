# GWH_TJ_WEMB
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
| 18 | WEMB_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WEMB_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WEMB_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WEMB_EOM_YMD | DATE | N | EOM Year And Month / EOM Year And Month |
| 22 | WEMB_PROD_COD | NVARCHAR2(100) | N | Product Code / Product Code |
| 23 | WEMB_ORGN_COD | CHAR(2) | N | Origin Code / Origin Code |
| 24 | WEMB_SBIV_COD | NVARCHAR2(40) | N | Sub Inventory / Sub Inventory |
| 25 | WEMB_PIK1 | NVARCHAR2(60) | N | Reference1 / ピッキングキー1 |
| 26 | WEMB_PIK2 | NVARCHAR2(60) | N | Reference2 / ピッキングキー2 |
| 27 | WEMB_PIK3 | NVARCHAR2(60) | N | Reference3 / ピッキングキー3 |
| 28 | WEMB_PIK4 | NVARCHAR2(60) | N | Reference4 / ピッキングキー4 |
| 29 | WEMB_PIK5 | NVARCHAR2(60) | N | Reference5 / ピッキングキー5 |
| 30 | WEMB_PIK6 | NVARCHAR2(60) | N | Reference6 / ピッキングキー6 |
| 31 | WEMB_PIK7 | NVARCHAR2(60) | N | Reference7 / ピッキングキー7 |
| 32 | WEMB_BMB_QTY | NUMBER(11) | Y | Beg of Month Balance / Beg of Month Balance |
| 33 | WEMB_EMB_QTY | NUMBER(11) | Y | End of Month Balance / End of Month Balance |
| 34 | WEMB_AVPC_QTY | NUMBER(9) | Y | Month Received / Month Received |
| 35 | WEMB_SPPC_QTY | NUMBER(9) | Y | Month Shipped / Month Shipped |
| 36 | WEMB_SCPC_QTY | NUMBER(9) | Y | Month Ship Cancel / Month Ship Cancel |
| 37 | WEMB_AJPC_QTY | NUMBER(9) | Y | Month Adjustment / Month Adjustment |
| 38 | WEMB_SIPC_QTY | NUMBER(9) | Y | Month S/I Change / Month S/I Change |
| 39 | WEMB_KTPC_QTY | NUMBER(9) | Y | Month Kitting Order / Month Kitting Order |
| 40 | WEMB_FLR1_QTY | NUMBER(9) | Y | 予備集計項目1 / filler 1 |
| 41 | WEMB_FLR2_QTY | NUMBER(9) | Y | 予備集計項目2 / filler 2 |
| 42 | WEMB_FLR3_QTY | NUMBER(9) | Y | 予備集計項目3 / filler 3 |
| 43 | WEMB_FLR4_QTY | NUMBER(9) | Y | 予備集計項目4 / filler 4 |
| 44 | WEMB_TOFG_FLG | CHAR(1) | Y | Take Over Flag / Take Over Flag |
| 45 | WEMB_PRDG_COD | NVARCHAR2(30) | Y | Product Group Code / 商品グループコード |
| 46 | WEMB_BDGD_FLG | CHAR(1) | N | Bond goods flag  / Bond goods flag  |
| 47 | WEMB_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 48 | WEMB_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 49 | WEMB_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 50 | WEMB_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 51 | WEMB_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 52 | WEMB_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 53 | WEMB_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 54 | WEMB_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 55 | WEMB_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 56 | WEMB_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 57 | WEMB_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 58 | WEMB_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 59 | WEMB_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 60 | WEMB_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 61 | WEMB_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 62 | WEMB_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 63 | WEMB_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 64 | WEMB_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 65 | WEMB_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 66 | WEMB_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 67 | WEMB_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 68 | WEMB_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 69 | WEMB_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 70 | WEMB_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 71 | WEMB_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 72 | WEMB_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 73 | WEMB_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 74 | WEMB_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 75 | WEMB_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 76 | WEMB_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
