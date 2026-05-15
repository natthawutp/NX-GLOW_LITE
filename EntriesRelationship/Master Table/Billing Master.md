# GWH_TM_BILL
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
| 18 | BILL_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | BILL_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | BILL_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | BILL_COD | NVARCHAR2(80) | N | Bill to code / 請求先コード |
| 22 | BILL_CRAC_COD | NVARCHAR2(40) | Y | Carrier Account code / キャリアアカウントコード |
| 23 | BILL_CRAC_NAM | NVARCHAR2(40) | Y | Carrier Account Name / キャリアアカウント名 |
| 24 | BILL_NAM1 | NVARCHAR2(100) | Y | Bill to name1 / 請求先名称1st |
| 25 | BILL_NAM2 | NVARCHAR2(100) | Y | Bill to name2 / 請求先名称2nd |
| 26 | BILL_JIS | NVARCHAR2(22) | Y | Charge Billing JIS / 請求先住所JISコード |
| 27 | BILL_ADR1 | NVARCHAR2(100) | Y | Bill to addr1 / 請求先住所1 |
| 28 | BILL_ADR2 | NVARCHAR2(100) | Y | Bill to addr2 / 請求先住所2 |
| 29 | BILL_ADR3 | NVARCHAR2(100) | Y | Bill to addr3 / 請求先住所3 |
| 30 | BILL_ADR4 | NVARCHAR2(100) | Y | Bill to addr4 / 請求先住所4 |
| 31 | BILL_ADR5 | NVARCHAR2(100) | Y | Bill to addr5 / 請求先住所5 |
| 32 | BILL_DPRT_NAM | NVARCHAR2(100) | Y | Bill to department name / 請求先部署名 |
| 33 | BILL_CON_NAM | NVARCHAR2(80) | Y | Bill to contact name / 請求先担当者名 |
| 34 | BILL_CTRY_COD | NVARCHAR2(4) | Y | Bill to country code / 請求先国コード |
| 35 | BILL_CITY_COD | NVARCHAR2(6) | Y | Bill to city code / 請求先都市コード |
| 36 | BILL_CITY_NAM | NVARCHAR2(100) | Y | Bill to city name / 請求先都市名 |
| 37 | BILL_STAT_COD | NVARCHAR2(4) | Y | Bill to state code / 請求先州コード |
| 38 | BILL_ZIP | NVARCHAR2(20) | Y | Bill to zip code / 請求先郵便番号 |
| 39 | BILL_TEL | NVARCHAR2(40) | Y | Bill to tel no / 請求先電話番号 |
| 40 | BILL_FAX_NUM | NVARCHAR2(40) | Y | Bill to fax number / 請求先FAX番号 |
| 41 | BILL_MAIL_ADR | NVARCHAR2(100) | Y | Bill to mail address / 請求先メールアドレス |
| 42 | BILL_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 43 | BILL_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 44 | BILL_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 45 | BILL_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 46 | BILL_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 47 | BILL_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 48 | BILL_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 49 | BILL_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 50 | BILL_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 51 | BILL_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 52 | BILL_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 53 | BILL_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 54 | BILL_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 55 | BILL_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 56 | BILL_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 57 | BILL_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 58 | BILL_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 59 | BILL_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 60 | BILL_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 61 | BILL_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 62 | BILL_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 63 | BILL_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 64 | BILL_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 65 | BILL_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 66 | BILL_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 67 | BILL_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 68 | BILL_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 69 | BILL_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 70 | BILL_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 71 | BILL_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 72 | BILL_CIT1 | NVARCHAR2(200) | Y | Customized Item 01 / 顧客専用項目01 |
| 73 | BILL_CIT2 | NVARCHAR2(200) | Y | Customized Item 02 / 顧客専用項目02 |
| 74 | BILL_CIT3 | NVARCHAR2(200) | Y | Customized Item 03 / 顧客専用項目03 |
| 75 | BILL_CIT4 | NVARCHAR2(200) | Y | Customized Item 04 / 顧客専用項目04 |
| 76 | BILL_CIT5 | NVARCHAR2(200) | Y | Customized Item 05 / 顧客専用項目05 |
| 77 | BILL_CIT6 | NVARCHAR2(200) | Y | Customized Item 06 / 顧客専用項目06 |
| 78 | BILL_CIT7 | NVARCHAR2(200) | Y | Customized Item 07 / 顧客専用項目07 |
| 79 | BILL_CIT8 | NVARCHAR2(200) | Y | Customized Item 08 / 顧客専用項目08 |
| 80 | BILL_CIT9 | NVARCHAR2(200) | Y | Customized Item 09 / 顧客専用項目09 |
| 81 | BILL_CIT10 | NVARCHAR2(200) | Y | Customized Item 10 / 顧客専用項目10 |
| 82 | BILL_CIT11 | NVARCHAR2(200) | Y | Customized Item 11 / 顧客専用項目11 |
| 83 | BILL_CIT12 | NVARCHAR2(200) | Y | Customized Item 12 / 顧客専用項目12 |
| 84 | BILL_CIT13 | NVARCHAR2(200) | Y | Customized Item 13 / 顧客専用項目13 |
| 85 | BILL_CIT14 | NVARCHAR2(200) | Y | Customized Item 14 / 顧客専用項目14 |
| 86 | BILL_CIT15 | NVARCHAR2(200) | Y | Customized Item 15 / 顧客専用項目15 |
