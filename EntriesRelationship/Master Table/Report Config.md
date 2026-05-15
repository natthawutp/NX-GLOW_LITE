# GWH_TM_RPTC
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
| 18 | RPTC_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | RPTC_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | RPTC_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | RPTC_RTP_KND | NVARCHAR2(6) | N | Report Kind / 帳票種別 |
| 22 | RPTC_ID_COD | NVARCHAR2(80) | N | Identification Code / 識別コード |
| 23 | RPTC_RPT_URL | NVARCHAR2(200) | N | Report URL / 帳票URL |
| 24 | RPTC_PRFX | NVARCHAR2(6) | Y | Prefix / プレフィックス |
| 25 | RPTC_STR_NUM | NVARCHAR2(50) | Y | Start No / 開始番号 |
| 26 | RPTC_END_NUM | NVARCHAR2(50) | Y | End No / 終了番号 |
| 27 | RPTC_CUR_NUM | NVARCHAR2(50) | Y | Current No / 現在番号 |
| 28 | RPTC_RMKS | NVARCHAR2(100) | Y | Remarks / 備考 |
| 29 | RPTC_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 30 | RPTC_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 31 | RPTC_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 32 | RPTC_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 33 | RPTC_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 34 | RPTC_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 35 | RPTC_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 36 | RPTC_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 37 | RPTC_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 38 | RPTC_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 39 | RPTC_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 40 | RPTC_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 41 | RPTC_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 42 | RPTC_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 43 | RPTC_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 44 | RPTC_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 45 | RPTC_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 46 | RPTC_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 47 | RPTC_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 48 | RPTC_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 49 | RPTC_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 50 | RPTC_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 51 | RPTC_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 52 | RPTC_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 53 | RPTC_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 54 | RPTC_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 55 | RPTC_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 56 | RPTC_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 57 | RPTC_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 58 | RPTC_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 59 | RPTC_CIT1 | NVARCHAR2(200) | Y | Customized Item 01 / 顧客専用項目01 |
| 60 | RPTC_CIT2 | NVARCHAR2(200) | Y | Customized Item 02 / 顧客専用項目02 |
| 61 | RPTC_CIT3 | NVARCHAR2(200) | Y | Customized Item 03 / 顧客専用項目03 |
| 62 | RPTC_CIT4 | NVARCHAR2(200) | Y | Customized Item 04 / 顧客専用項目04 |
| 63 | RPTC_CIT5 | NVARCHAR2(200) | Y | Customized Item 05 / 顧客専用項目05 |
| 64 | RPTC_CIT6 | NVARCHAR2(200) | Y | Customized Item 06 / 顧客専用項目06 |
| 65 | RPTC_CIT7 | NVARCHAR2(200) | Y | Customized Item 07 / 顧客専用項目07 |
| 66 | RPTC_CIT8 | NVARCHAR2(200) | Y | Customized Item 08 / 顧客専用項目08 |
| 67 | RPTC_CIT9 | NVARCHAR2(200) | Y | Customized Item 09 / 顧客専用項目09 |
| 68 | RPTC_CIT10 | NVARCHAR2(200) | Y | Customized Item 10 / 顧客専用項目10 |
| 69 | RPTC_CIT11 | NVARCHAR2(200) | Y | Customized Item 11 / 顧客専用項目11 |
| 70 | RPTC_CIT12 | NVARCHAR2(200) | Y | Customized Item 12 / 顧客専用項目12 |
| 71 | RPTC_CIT13 | NVARCHAR2(200) | Y | Customized Item 13 / 顧客専用項目13 |
| 72 | RPTC_CIT14 | NVARCHAR2(200) | Y | Customized Item 14 / 顧客専用項目14 |
| 73 | RPTC_CIT15 | NVARCHAR2(200) | Y | Customized Item 15 / 顧客専用項目15 |
| 74 | RPTC_PRTC_FLG | CHAR(1) | N | Print Carton Label Flag / カートンラベル印刷フラグ |
