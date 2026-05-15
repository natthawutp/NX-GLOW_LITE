# GWH_TJ_LPR_HIS
#Entity #Standard #HISTORY

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
| 18 | LPR_HIS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LPR_HIS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LPR_HIS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LPR_HIS_SEQ | NVARCHAR2(20) | N | History Seq / 印刷履歴連番 |
| 22 | LPR_HIS_LINE_NUM | NUMBER(2) | N | History Line Num / 印刷履歴ライン番号 |
| 23 | LPR_HIS_UUID | NVARCHAR2(100) | Y | UUID / UUID |
| 24 | LPR_HIS_PRT_PRD_TYPE | NVARCHAR2(36) | Y | Printer Product Type / プリンタ機種 |
| 25 | LPR_HIS_MDL_NUM | NVARCHAR2(100) | Y | Printer Model Number / プリンタ型式 |
| 26 | LPR_HIS_PRT_ID | NVARCHAR2(12) | Y | Printer Id / プリンタID |
| 27 | LPR_HIS_IP_ADR | NVARCHAR2(40) | Y | IP Address / IPアドレス |
| 28 | LPR_HIS_PORT_NUM | NUMBER(5) | Y | Port / ポート番号 |
| 29 | LPR_HIS_FRM_ID | NVARCHAR2(36) | Y | Form ID / フォームID |
| 30 | LPR_HIS_PRT_CMD | NVARCHAR2(600) | Y | Print Command / 印刷指示内容 |
| 31 | LPR_HIS_PRT_YMDHMS | TIMESTAMP(6) | Y | Print / 印刷指示日時 |
| 32 | LPR_HIS_PRT_RTN_COD | NVARCHAR2(6) | Y | Printer Return Code / プリンタ返却値 |
| 33 | LPR_HIS_RESPONSE | NVARCHAR2(200) | Y | Respons / 返却値詳細 |
| 34 | LPR_HIS_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 35 | LPR_HIS_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 36 | LPR_HIS_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 37 | LPR_HIS_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 38 | LPR_HIS_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 39 | LPR_HIS_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 40 | LPR_HIS_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 41 | LPR_HIS_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 42 | LPR_HIS_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 43 | LPR_HIS_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 44 | LPR_HIS_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 45 | LPR_HIS_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 46 | LPR_HIS_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 47 | LPR_HIS_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 48 | LPR_HIS_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 49 | LPR_HIS_COI1 | NVARCHAR2(200) | Y | Customized only Item 01 / カスタマイズ項目01 |
| 50 | LPR_HIS_COI2 | NVARCHAR2(200) | Y | Customized only Item 02 / カスタマイズ項目02 |
| 51 | LPR_HIS_COI3 | NVARCHAR2(200) | Y | Customized only Item 03 / カスタマイズ項目03 |
| 52 | LPR_HIS_COI4 | NVARCHAR2(200) | Y | Customized only Item 04 / カスタマイズ項目04 |
| 53 | LPR_HIS_COI5 | NVARCHAR2(200) | Y | Customized only Item 05 / カスタマイズ項目05 |
| 54 | LPR_HIS_COIL1 | NVARCHAR2(510) | Y | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| 55 | LPR_HIS_COIL2 | NVARCHAR2(510) | Y | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| 56 | LPR_HIS_COIL3 | NVARCHAR2(510) | Y | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| 57 | LPR_HIS_COIL4 | NVARCHAR2(510) | Y | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| 58 | LPR_HIS_COIL5 | NVARCHAR2(510) | Y | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
