# GWH_TJ_WMBF
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
| 18 | WMBF_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WMBF_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WMBF_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WMBF_EOM_YMD | DATE | N | EOM Year And Month / EOM Year And Month |
| 22 | WMBF_PROD_COD | NVARCHAR2(100) | N | Product Code / Product Code |
| 23 | WMBF_ORGN_COD | CHAR(2) | N | Origin Code / Origin Code |
| 24 | WMBF_SBIV_COD | NVARCHAR2(40) | N | Sub Inventory / Sub Inventory |
| 25 | WMBF_DMG_FLG | CHAR(1) | N | Damage/Hold Flag / ダメージ/ホールドフラグ |
| 26 | WMBF_PIK1 | NVARCHAR2(60) | N | Reference1 / ピッキングキー1 |
| 27 | WMBF_PIK2 | NVARCHAR2(60) | N | Reference2 / ピッキングキー2 |
| 28 | WMBF_PIK3 | NVARCHAR2(60) | N | Reference3 / ピッキングキー3 |
| 29 | WMBF_PIK4 | NVARCHAR2(60) | N | Reference4 / ピッキングキー4 |
| 30 | WMBF_PIK5 | NVARCHAR2(60) | N | Reference5 / ピッキングキー5 |
| 31 | WMBF_PIK6 | NVARCHAR2(60) | N | Reference6 / ピッキングキー6 |
| 32 | WMBF_PIK7 | NVARCHAR2(60) | N | Reference7 / ピッキングキー7 |
| 33 | WMBF_BMB_QTY | NUMBER(11) | Y | Beg of Month Balance / Beg of Month Balance |
| 34 | WMBF_EMB_QTY | NUMBER(11) | Y | End of Month Balance / End of Month Balance |
| 35 | WMBF_AVPC_QTY | NUMBER(9) | Y | Month Received / Month Received |
| 36 | WMBF_ANPC_QTY | NUMBER(9) | Y | Month Received(Not applicable) / Month Received(Not applicable) |
| 37 | WMBF_SPPC_QTY | NUMBER(9) | Y | Month Shipped / Month Shipped |
| 38 | WMBF_SNPC_QTY | NUMBER(9) | Y | Month Ship(Not applicable) / Month Ship(Not applicable) |
| 39 | WMBF_AJPC_QTY | NUMBER(9) | Y | Month Adjustment / Month Adjustment |
| 40 | WMBF_SIPC_QTY | NUMBER(9) | Y | Month S/I Change / Month S/I Change |
| 41 | WMBF_DHPC_QTY | NUMBER(9) | Y | Month D/H Change / Month D/H Change |
| 42 | WMBF_PKPC_QTY | NUMBER(9) | Y | Month Pik Change / Month Pik Change |
| 43 | WMBF_KTPC_QTY | NUMBER(9) | Y | Month Kitting Order / Month Kitting Order |
| 44 | WMBF_FLR1_QTY | NUMBER(9) | Y | 予備集計項目1 / filler 1 |
| 45 | WMBF_FLR2_QTY | NUMBER(9) | Y | 予備集計項目2 / filler 2 |
| 46 | WMBF_FLR3_QTY | NUMBER(9) | Y | 予備集計項目3 / filler 3 |
| 47 | WMBF_FLR4_QTY | NUMBER(9) | Y | 予備集計項目4 / filler 4 |
| 48 | WMBF_TOFG_FLG | CHAR(1) | Y | Take Over Flag / Take Over Flag |
| 49 | WMBF_PRDG_COD | NVARCHAR2(30) | Y | Product Group Code / 商品グループコード |
| 50 | WMBF_BDGD_FLG | CHAR(1) | N | Bond goods flag  / Bond goods flag  |
| 51 | WMBF_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 52 | WMBF_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 53 | WMBF_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 54 | WMBF_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 55 | WMBF_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 56 | WMBF_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 57 | WMBF_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 58 | WMBF_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 59 | WMBF_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 60 | WMBF_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 61 | WMBF_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 62 | WMBF_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 63 | WMBF_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 64 | WMBF_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 65 | WMBF_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 66 | WMBF_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 67 | WMBF_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 68 | WMBF_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 69 | WMBF_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 70 | WMBF_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 71 | WMBF_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 72 | WMBF_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 73 | WMBF_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 74 | WMBF_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 75 | WMBF_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 76 | WMBF_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 77 | WMBF_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 78 | WMBF_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 79 | WMBF_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 80 | WMBF_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
