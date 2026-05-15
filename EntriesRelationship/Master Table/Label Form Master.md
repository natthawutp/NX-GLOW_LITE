# GWH_TM_LFRM
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
| 18 | LFRM_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LFRM_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LFRM_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LFRM_FRM_ID | NVARCHAR2(36) | N | Form ID / フォームID |
| 22 | LFRM_PRT_PRD_TYPE | NVARCHAR2(36) | Y | Printer Product Type / プリンタ機種 |
| 23 | LFRM_MDL_NUM | NVARCHAR2(100) | Y | Printer Model Number / プリンタ型式 |
| 24 | LFRM_LBL_NAM | NVARCHAR2(120) | Y | Form Name / ラベル名 |
| 25 | LFRM_FILE_PATH | NVARCHAR2(400) | Y | Label File Path / ラベルファイルパス |
| 26 | LFRM_FILE_NAM | NVARCHAR2(60) | Y | Label File Name / ラベルファイル名 |
| 27 | LFRM_DLMTR | NVARCHAR2(2) | Y | Delimiter / 区切り文字 |
| 28 | LFRM_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 29 | LFRM_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 30 | LFRM_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 31 | LFRM_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 32 | LFRM_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 33 | LFRM_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 34 | LFRM_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 35 | LFRM_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 36 | LFRM_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 37 | LFRM_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 38 | LFRM_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 39 | LFRM_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 40 | LFRM_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 41 | LFRM_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 42 | LFRM_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 43 | LFRM_COI1 | NVARCHAR2(200) | Y | Customized only Item 01 / カスタマイズ項目01 |
| 44 | LFRM_COI2 | NVARCHAR2(200) | Y | Customized only Item 02 / カスタマイズ項目02 |
| 45 | LFRM_COI3 | NVARCHAR2(200) | Y | Customized only Item 03 / カスタマイズ項目03 |
| 46 | LFRM_COI4 | NVARCHAR2(200) | Y | Customized only Item 04 / カスタマイズ項目04 |
| 47 | LFRM_COI5 | NVARCHAR2(200) | Y | Customized only Item 05 / カスタマイズ項目05 |
| 48 | LFRM_COIL1 | NVARCHAR2(510) | Y | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| 49 | LFRM_COIL2 | NVARCHAR2(510) | Y | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| 50 | LFRM_COIL3 | NVARCHAR2(510) | Y | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| 51 | LFRM_COIL4 | NVARCHAR2(510) | Y | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| 52 | LFRM_COIL5 | NVARCHAR2(510) | Y | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
| 53 | LFRM_FILE_PATH1 | NVARCHAR2(400) | Y | Add Command File Path1 / 追加コマンドパス1 |
| 54 | LFRM_FILE_PATH2 | NVARCHAR2(400) | Y | Add Command File Path2 / 追加コマンドパス2 |
| 55 | LFRM_FILE_PATH3 | NVARCHAR2(400) | Y | Add Command File Path3 / 追加コマンドパス3 |
| 56 | LFRM_FILE_PATH4 | NVARCHAR2(400) | Y | Add Command File Path4 / 追加コマンドパス4 |
| 57 | LFRM_FILE_PATH5 | NVARCHAR2(400) | Y | Add Command File Path5 / 追加コマンドパス5 |
