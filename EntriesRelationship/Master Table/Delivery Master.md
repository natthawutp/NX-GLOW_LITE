# GWH_TM_DLV
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
| 18 | DLV_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DLV_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DLV_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DLV_COD | NVARCHAR2(80) | N | Delivery To Code / 配送先コード |
| 22 | DLV_NAM1 | NVARCHAR2(100) | Y | Delivery To Name 1St / 配送先名称1st |
| 23 | DLV_NAM2 | NVARCHAR2(100) | Y | Delivery To Name 2Nd / 配送先名称2nd |
| 24 | DLV_TEL | NVARCHAR2(40) | Y | Delivery To Tel / 配送先電話番号 |
| 25 | DLV_ZIP | NVARCHAR2(20) | Y | Delivery To Zip / 配送先郵便番号 |
| 26 | DLV_JIS | NVARCHAR2(22) | Y | Delivery To Jis / 配送先住所コード |
| 27 | DLV_ADR1 | NVARCHAR2(100) | Y | Delivery To Address1 / 配送先住所1 |
| 28 | DLV_ADR2 | NVARCHAR2(100) | Y | Delivery To Address2 / 配送先住所2 |
| 29 | DLV_ADR3 | NVARCHAR2(100) | Y | Delivery To Address3 / 配送先住所3 |
| 30 | DLV_ADR4 | NVARCHAR2(100) | Y | Delivery To Address4 / 配送先住所4 |
| 31 | DLV_ADR5 | NVARCHAR2(100) | Y | Delivery To Address5 / 配送先住所5 |
| 32 | DLV_DPRT_NAM | NVARCHAR2(100) | Y | Delivery To Department Name / 配送先部署名 |
| 33 | DLV_REP_NAM | NVARCHAR2(100) | Y | Delivery To Representative / 配送先担当者 |
| 34 | DLV_MAL | NVARCHAR2(200) | Y | Delivery To Mail Address / 配送先メールアドレス |
| 35 | DLV_VAT_COD | CHAR(2) | Y | Vat Registration Country / VAT_REGISTRATION_COUNTRY |
| 36 | DLV_VAT_NUM | CHAR(15) | Y | Vat Registration No / VAT_REGISTRATION_NO. |
| 37 | DLV_LPIK_FLG | CHAR(1) | N | Last Shipping or Delivery Control Flag / 最新ピックキー管理フラグ |
| 38 | DLV_PKDP_FLG | CHAR(1) | N | Pick-Key Devide Prohibition Flag / ピックキー分割禁止フラグ |
| 39 | DLV_TRPT_COD | NVARCHAR2(10) | Y | Transport Mode / 輸送モード |
| 40 | DLV_ROUT_COD | NVARCHAR2(10) | Y | Route Code / ルートコード |
| 41 | DLV_CAR_NUM | NVARCHAR2(24) | Y | Car Number / 車番 |
| 42 | DLV_DVPR | NUMBER(4) | Y | Delivery Priority Order / 配送先優先順位 |
| 43 | DLV_CDLV_KND | CHAR(2) | Y | Cargo Deliver Kind / 貨物引渡区分 |
| 44 | DLV_ISLD_COD | CHAR(3) | Y | Island Code / 離島コード |
| 45 | DLV_RPRM_KND | CHAR(1) | Y | Region Premium Kind / 地域割増適用区分 |
| 46 | DLV_WPRM_KND | CHAR(1) | Y | Winter Premium Kind / 季節割増適用区分 |
| 47 | DLV_RLAY | NUMBER(3) | Y | Number Of Relay / 連絡中継回数 |
| 48 | DLV_ARWD_WAY | NUMBER(6) | Y | Delivery Distance / 配達距離 |
| 49 | DLV_ARWS_WAY | NUMBER(6) | Y | Business Distance / 営業距離 |
| 50 | DLV_NTFY_NAM1 | NVARCHAR2(100) | Y | Notify Name 1st / 連絡先名称1st |
| 51 | DLV_NTFY_NAM2 | NVARCHAR2(100) | Y | Notify Name 2nd / 連絡先名称2nd |
| 52 | DLV_NTFY_ADR1 | NVARCHAR2(100) | Y | Notify Address1 / 連絡先住所1 |
| 53 | DLV_NTFY_ADR2 | NVARCHAR2(100) | Y | Notify Address2 / 連絡先住所2 |
| 54 | DLV_NTFY_ADR3 | NVARCHAR2(100) | Y | Notify Address3 / 連絡先住所3 |
| 55 | DLV_NTFY_ADR4 | NVARCHAR2(100) | Y | Notify Address4 / 連絡先住所4 |
| 56 | DLV_NTFY_ADR5 | NVARCHAR2(100) | Y | Notify Address5 / 連絡先住所5 |
| 57 | DLV_NTFY_TEL | NVARCHAR2(40) | Y | Notify Telephone / 連絡先電話番号 |
| 58 | DLV_NTFY_ZIP | NVARCHAR2(20) | Y | Notify Zip / 連絡先郵便番号 |
| 59 | DLV_NTFY_RMKS | NVARCHAR2(100) | Y | Notify Attention / 連絡先備考 |
| 60 | DLV_CTRY_COD | NVARCHAR2(4) | Y | Country Code / 国コード |
| 61 | DLV_CITY_COD | NVARCHAR2(6) | Y | City Code / 都市コード |
| 62 | DLV_CITY_NAM | NVARCHAR2(100) | Y | City Name / 都市名 |
| 63 | DLV_STAT_COD | NVARCHAR2(4) | Y | State Code / 州コード |
| 64 | DLV_ALOT_REVS_FLG | CHAR(1) | N | Allow Lot Reversal |
| 65 | DLV_DEST_REQ | NVARCHAR2(200) | Y | Destination Requirements |
| 66 | DLV_CUSS_SLIP_FLG | CHAR(1) | N | Customer-Specific Slip |
| 67 | DLV_NOTP_FLG | CHAR(1) | N | Delivery Note Print Flag |
| 68 | DLV_LABP_FLG | CHAR(1) | N | Delivery Label Print Flag |
| 69 | DLV_STSP_FLG | CHAR(1) | N | Staging Sheet Print Flag |
| 70 | DLV_LDLP_FLG | CHAR(1) | N | Loading List Print Flag |
| 71 | DLV_OROF_STG | NUMBER(3) | Y | Order of Staging |
| 72 | DLV_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 73 | DLV_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 74 | DLV_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 75 | DLV_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 76 | DLV_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 77 | DLV_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 78 | DLV_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 79 | DLV_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 80 | DLV_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 81 | DLV_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 82 | DLV_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 83 | DLV_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 84 | DLV_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 85 | DLV_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 86 | DLV_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 87 | DLV_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 88 | DLV_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 89 | DLV_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 90 | DLV_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 91 | DLV_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 92 | DLV_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 93 | DLV_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 94 | DLV_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 95 | DLV_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 96 | DLV_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 97 | DLV_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 98 | DLV_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 99 | DLV_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 100 | DLV_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 101 | DLV_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 102 | DLV_CIT1 | NVARCHAR2(200) | Y | Customized Item 01 / 顧客専用項目01 |
| 103 | DLV_CIT2 | NVARCHAR2(200) | Y | Customized Item 02 / 顧客専用項目02 |
| 104 | DLV_CIT3 | NVARCHAR2(200) | Y | Customized Item 03 / 顧客専用項目03 |
| 105 | DLV_CIT4 | NVARCHAR2(200) | Y | Customized Item 04 / 顧客専用項目04 |
| 106 | DLV_CIT5 | NVARCHAR2(200) | Y | Customized Item 05 / 顧客専用項目05 |
| 107 | DLV_CIT6 | NVARCHAR2(200) | Y | Customized Item 06 / 顧客専用項目06 |
| 108 | DLV_CIT7 | NVARCHAR2(200) | Y | Customized Item 07 / 顧客専用項目07 |
| 109 | DLV_CIT8 | NVARCHAR2(200) | Y | Customized Item 08 / 顧客専用項目08 |
| 110 | DLV_CIT9 | NVARCHAR2(200) | Y | Customized Item 09 / 顧客専用項目09 |
| 111 | DLV_CIT10 | NVARCHAR2(200) | Y | Customized Item 10 / 顧客専用項目10 |
| 112 | DLV_CIT11 | NVARCHAR2(200) | Y | Customized Item 11 / 顧客専用項目11 |
| 113 | DLV_CIT12 | NVARCHAR2(200) | Y | Customized Item 12 / 顧客専用項目12 |
| 114 | DLV_CIT13 | NVARCHAR2(200) | Y | Customized Item 13 / 顧客専用項目13 |
| 115 | DLV_CIT14 | NVARCHAR2(200) | Y | Customized Item 14 / 顧客専用項目14 |
| 116 | DLV_CIT15 | NVARCHAR2(200) | Y | Customized Item 15 / 顧客専用項目15 |
| 117 | DLV_RCV_BTCH_NUM | NVARCHAR2(60) | Y | Recive Batch Number / 受信バッチNo |
