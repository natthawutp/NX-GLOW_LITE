# GWH_TM_LPR
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
| 18 | LPR_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LPR_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LPR_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LPR_PRT_PRD_TYPE | NVARCHAR2(36) | N | Printer Product Type / プリンタ機種 |
| 22 | LPR_MDL_NUM | NVARCHAR2(100) | N | Printer Model Number / プリンタ型式 |
| 23 | LPR_PRT_ID | NVARCHAR2(12) | N | Printer Id / プリンタID |
| 24 | LPR_MKR_COD | NVARCHAR2(6) | N | Maker Code / メーカーコード |
| 25 | LPR_DPI | NUMBER(4) | N | Dot Per Inch / DPI |
| 26 | LPR_DISP_NAM | NVARCHAR2(80) | Y | Printer Display Name / プリンタ表示名 |
| 27 | LPR_COM_KND | NVARCHAR2(4) | Y | Communication Kind / 通信種別 |
| 28 | LPR_IP_ADR | NVARCHAR2(40) | N | IP Address / IPアドレス |
| 29 | LPR_PORT_NUM | NUMBER(5) | N | Port　Number / ポート番号 |
| 30 | LPR_REG_DATE | TIMESTAMP(6) | Y | Printer Regist Date / プリンタ登録日 |
| 31 | LPR_DEL_DATE | TIMESTAMP(6) | Y | Printer Delete Date / プリンタ削除予定日 |
| 32 | LPR_SEND_TIMEOUT | NUMBER(9) | N | Send Timeout / 送信タイムアウト値 |
| 33 | LPR_READ_TIMEOUT | NUMBER(9) | N | Read Timeout / 受信タイムアウト値 |
| 34 | LPR_RMK1 | NVARCHAR2(400) | Y | Remark 1 / 備考1 |
| 35 | LPR_RMK2 | NVARCHAR2(400) | Y | Remark 2 / 備考2 |
| 36 | LPR_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 37 | LPR_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 38 | LPR_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 39 | LPR_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 40 | LPR_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 41 | LPR_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 42 | LPR_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 43 | LPR_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 44 | LPR_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 45 | LPR_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 46 | LPR_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 47 | LPR_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 48 | LPR_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 49 | LPR_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 50 | LPR_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 51 | LPR_COI1 | NVARCHAR2(200) | Y | Customized only Item 01 / カスタマイズ項目01 |
| 52 | LPR_COI2 | NVARCHAR2(200) | Y | Customized only Item 02 / カスタマイズ項目02 |
| 53 | LPR_COI3 | NVARCHAR2(200) | Y | Customized only Item 03 / カスタマイズ項目03 |
| 54 | LPR_COI4 | NVARCHAR2(200) | Y | Customized only Item 04 / カスタマイズ項目04 |
| 55 | LPR_COI5 | NVARCHAR2(200) | Y | Customized only Item 05 / カスタマイズ項目05 |
| 56 | LPR_COIL1 | NVARCHAR2(510) | Y | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| 57 | LPR_COIL2 | NVARCHAR2(510) | Y | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| 58 | LPR_COIL3 | NVARCHAR2(510) | Y | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| 59 | LPR_COIL4 | NVARCHAR2(510) | Y | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| 60 | LPR_COIL5 | NVARCHAR2(510) | Y | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
