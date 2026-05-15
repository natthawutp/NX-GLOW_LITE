# GWH_TM_LPPA_PAIR
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
| 18 | LPPA_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LPPA_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LPPA_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LPPA_UUID | NVARCHAR2(100) | N | UUID / UUID |
| 22 | LPPA_FRM_ID | NVARCHAR2(36) | N | Form ID / フォームID |
| 23 | LPPA_PRT_PRD_TYPE | NVARCHAR2(36) | N | Printer Product Type / プリンタ機種 |
| 24 | LPPA_MDL_NUM | NVARCHAR2(100) | N | Printer Model / プリンタ型式 |
| 25 | LPPA_PRT_ID | NVARCHAR2(12) | N | Printer Id / プリンタID |
| 26 | LPPA_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 27 | LPPA_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 28 | LPPA_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 29 | LPPA_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 30 | LPPA_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 31 | LPPA_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 32 | LPPA_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 33 | LPPA_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 34 | LPPA_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 35 | LPPA_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 36 | LPPA_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 37 | LPPA_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 38 | LPPA_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 39 | LPPA_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 40 | LPPA_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 41 | LPPA_COI1 | NVARCHAR2(200) | Y | Customized only Item 01 / カスタマイズ項目01 |
| 42 | LPPA_COI2 | NVARCHAR2(200) | Y | Customized only Item 02 / カスタマイズ項目02 |
| 43 | LPPA_COI3 | NVARCHAR2(200) | Y | Customized only Item 03 / カスタマイズ項目03 |
| 44 | LPPA_COI4 | NVARCHAR2(200) | Y | Customized only Item 04 / カスタマイズ項目04 |
| 45 | LPPA_COI5 | NVARCHAR2(200) | Y | Customized only Item 05 / カスタマイズ項目05 |
| 46 | LPPA_COIL1 | NVARCHAR2(510) | Y | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| 47 | LPPA_COIL2 | NVARCHAR2(510) | Y | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| 48 | LPPA_COIL3 | NVARCHAR2(510) | Y | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| 49 | LPPA_COIL4 | NVARCHAR2(510) | Y | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| 50 | LPPA_COIL5 | NVARCHAR2(510) | Y | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
| 51 | LPPA_USE_FLG | CHAR(1) | N | Printer Id / 直接印刷使用フラグ |
