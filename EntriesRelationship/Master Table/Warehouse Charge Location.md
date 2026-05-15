# GWH_TM_WLC
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
| 18 | WLC_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WLC_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WLC_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WLC_HDFG_TYP | CHAR(1) | N | Header/Detail Type / Header/Detail Type |
| 22 | WLC_PROD_COD | NVARCHAR2(100) | N | Product Code / Product Code |
| 23 | WLC_ORGN_COD | CHAR(2) | N | Origin Code / Origin Code |
| 24 | WLC_SBIV_COD | NVARCHAR2(40) | N | Sub Inventory / Sub Inventory |
| 25 | WLC_CHTP_TYP | CHAR(1) | N | Charge Type / Charge Type |
| 26 | WLC_CHCD_COD | CHAR(4) | N | Charge Code / Charge Code |
| 27 | WLC_RTCD_COD | CHAR(2) | N | Rate Code / Rate Code |
| 28 | WLC_RAT1_AMT | NUMBER(14,4) | Y | Rate 1 / Rate 1 |
| 29 | WLC_TRN1_AMT | NUMBER(14,4) | Y | Transaction Minimum 1 / Transaction Minimum 1 |
| 30 | WLC_CAS1_AMT | NUMBER(14,4) | Y | Case Minimum 1 / Case Minimum 1 |
| 31 | WLC_FMY1 | DATE | Y | From Year and Month 1 / From Year and Month 1 |
| 32 | WLC_RAT2_AMT | NUMBER(14,4) | Y | Rate 2 / Rate 2 |
| 33 | WLC_TRN2_AMT | NUMBER(14,4) | Y | Transaction Minimum 2 / Transaction Minimum 2 |
| 34 | WLC_CAS2_AMT | NUMBER(14,4) | Y | Case Minimum 2 / Case Minimum 2 |
| 35 | WLC_FMY2 | DATE | Y | From Year and Month 2 / From Year and Month 2 |
| 36 | WLC_RAT3_AMT | NUMBER(14,4) | Y | Rate 3 / Rate 3 |
| 37 | WLC_TRN3_AMT | NUMBER(14,4) | Y | Transaction Minimum 3 / Transaction Minimum 3 |
| 38 | WLC_CAS3_AMT | NUMBER(14,4) | Y | Case Minimum 3 / Case Minimum 3 |
| 39 | WLC_FMY3 | DATE | Y | From Year and Month 3 / From Year and Month 3 |
| 40 | WLC_SCTP_TYP | CHAR(2) | Y | Selection Carrier Type  / Selection Carrier Type  |
| 41 | WLC_SCCD_COD | NVARCHAR2(10) | Y | Selection Carrier Code / Selection Carrier Code |
| 42 | WLC_SOTP_TYP | CHAR(2) | Y | Selection Type / Selection Type |
| 43 | WLC_SOCD_COD | NVARCHAR2(20) | Y | Selection Code / Selection Code |
| 44 | WLC_STRG_FLG | CHAR(1) | Y | Storage Flag / Storage Flag |
| 45 | WLC_STFG_TYP | CHAR(1) | Y | Storage Charge Term Type / Storage Charge Term Type |
| 46 | WLC_STDW | CHAR(3) | Y | Storage Day of Week / Storage Day of Week |
| 47 | WLC_STRT1_RAT | NUMBER(14,4) | Y | Term1 Rate (%) / Term1 Rate (%) |
| 48 | WLC_STRT2_RAT | NUMBER(14,4) | Y | Term2 Rate (%) / Term2 Rate (%) |
| 49 | WLC_STRT3_RAT | NUMBER(14,4) | Y | Term3 Rate (%) / Term3 Rate (%) |
| 50 | WLC_STRT4_RAT | NUMBER(14,4) | Y | Term4 Rate (%) / Term4 Rate (%) |
| 51 | WLC_STRT5_RAT | NUMBER(14,4) | Y | Term5 Rate (%) / Term5 Rate (%) |
| 52 | WLC_RDFG_TYP | CHAR(1) | Y | Round Up Type / Round Up Type |
| 53 | WLC_CFG1_FLG | CHAR(1) | Y | Customized Flag 01 / 顧客専用フラグ01 |
| 54 | WLC_CFG2_FLG | CHAR(1) | Y | Customized Flag 02 / 顧客専用フラグ02 |
| 55 | WLC_CFG3_FLG | CHAR(1) | Y | Customized Flag 03 / 顧客専用フラグ03 |
| 56 | WLC_CFG4_FLG | CHAR(1) | Y | Customized Flag 04 / 顧客専用フラグ04 |
| 57 | WLC_CFG5_FLG | CHAR(1) | Y | Customized Flag 05 / 顧客専用フラグ05 |
| 58 | WLC_CFG6_FLG | CHAR(1) | Y | Customized Flag 06 / 顧客専用フラグ06 |
| 59 | WLC_CFG7_FLG | CHAR(1) | Y | Customized Flag 07 / 顧客専用フラグ07 |
| 60 | WLC_CFG8_FLG | CHAR(1) | Y | Customized Flag 08 / 顧客専用フラグ08 |
| 61 | WLC_CFG9_FLG | CHAR(1) | Y | Customized Flag 09 / 顧客専用フラグ09 |
| 62 | WLC_CFG10_FLG | CHAR(1) | Y | Customized Flag 10 / 顧客専用フラグ10 |
| 63 | WLC_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 64 | WLC_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 65 | WLC_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 66 | WLC_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 67 | WLC_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 68 | WLC_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 69 | WLC_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 70 | WLC_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 71 | WLC_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 72 | WLC_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 73 | WLC_CNI1_NUM | NUMBER(14,4) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 74 | WLC_CNI2_NUM | NUMBER(14,4) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 75 | WLC_CNI3_NUM | NUMBER(14,4) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 76 | WLC_CNI4_NUM | NUMBER(14,4) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 77 | WLC_CNI5_NUM | NUMBER(14,4) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 78 | WLC_CNI6_NUM | NUMBER(14,4) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 79 | WLC_CNI7_NUM | NUMBER(14,4) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 80 | WLC_CNI8_NUM | NUMBER(14,4) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 81 | WLC_CNI9_NUM | NUMBER(14,4) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 82 | WLC_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
