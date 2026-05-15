# GWH_TJ_AV_BD
#Entity #Standard #INBOUND

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
| 18 | AVBD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | AVBD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | AVBD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | AVBD_AV_NUM | NVARCHAR2(12) | N | Arrival Number / 入荷番号 |
| 22 | AVBD_AVLN_NUM | NUMBER(4) | N | Arrival Line No / 入荷ラインNo |
| 23 | AVBD_PCUR_COD | CHAR(3) | Y | Price Currency (Product) / 通貨 (商品) |
| 24 | AVBD_PUPR_AMN | NUMBER(15,5) | Y | Foreign Unit Price (Product) / 外貨単価 (商品) |
| 25 | AVBD_PEXR_RAT | NUMBER(15,5) | Y | Exchange Rate (Product) / レート (商品) |
| 26 | AVBD_FCUR_COD | CHAR(3) | Y | Price Currency (Freight) / 通貨 (運賃) |
| 27 | AVBD_FUPR_AMN | NUMBER(15,5) | Y | Foreign Unit Price (Freight) / 外貨単価 (運賃) |
| 28 | AVBD_FEXR_RAT | NUMBER(15,5) | Y | Exchange Rate (Freight) / レート (運賃) |
| 29 | AVBD_ICUR_COD | CHAR(3) | Y | Price Currency (Insurance) / 通貨 (保険) |
| 30 | AVBD_IUPR_AMN | NUMBER(15,5) | Y | Foreign Unit Price (Insurance) / 外貨単価 (保険) |
| 31 | AVBD_IEXR_RAT | NUMBER(15,5) | Y | Exchange Rate (Insurance) / レート (保険) |
| 32 | AVBD_DUTY_RAT | NUMBER(4,2) | Y | Duty Rate (%) / Duty レート (%) |
| 33 | AVBD_DCUR_COD | CHAR(3) | Y | Duty Currency / Duty 通貨 |
| 34 | AVBD_DUTY_AMN | NUMBER(12,2) | Y | Duty Ammount / Duty 金額 |
| 35 | AVBD_VAT_RAT | NUMBER(4,2) | Y | VAT Rate (%) / VAT  レート (%) |
| 36 | AVBD_VCUR_COD | CHAR(3) | Y | VAT Currency / VAT 通貨 |
| 37 | AVBD_VAT_AMN | NUMBER(12,2) | Y | VAT Ammount / VAT 金額 |
| 38 | AVBD_VADJ_RAT | NUMBER(4,2) | Y | VAT Adjustment Rate (%) / VAT調整  レート (%) |
| 39 | AVBD_VACR_COD | CHAR(3) | Y | VAT Adjustment Currency / VAT調整 通貨 |
| 40 | AVBD_VADJ_AMN | NUMBER(12,2) | Y | VAT Adjustmnt Ammount / VAT調整 金額 |
| 41 | AVBD_TAX_COD | NVARCHAR2(4) | Y | TAX Code / TAX コード |
| 42 | AVBD_TAX_RAT | NUMBER(4,2) | Y | TAX Rate (%) / TAX レート (%) |
| 43 | AVBD_TCUR_COD | CHAR(3) | Y | TAX Currency / TAX 通貨 |
| 44 | AVBD_TAX_AMN | NUMBER(12,2) | Y | TAX Ammount / TAX 金額 |
| 45 | AVBD_LCNC_NUM | NVARCHAR2(60) | Y | Licence No / ライセンスNo |
| 46 | AVBD_HS_COD | NVARCHAR2(36) | Y | HS CODE / HS CODE |
| 47 | AVBD_WGT | NUMBER(12,6) | Y | Weight / 重量 |
| 48 | AVBD_M3 | NUMBER(12,6) | Y | Volume / 容積 |
| 49 | AVBD_RMKS | NVARCHAR2(400) | Y | Remark / 明細備考 |
| 50 | AVBD_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 51 | AVBD_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 52 | AVBD_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 53 | AVBD_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 54 | AVBD_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 55 | AVBD_CLI6 | NVARCHAR2(100) | Y | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| 56 | AVBD_CLI7 | NVARCHAR2(100) | Y | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| 57 | AVBD_CLI8 | NVARCHAR2(100) | Y | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| 58 | AVBD_CLI9 | NVARCHAR2(100) | Y | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| 59 | AVBD_CLI10 | NVARCHAR2(100) | Y | Customized List Items for printing 10 / 顧客専用リスト印字用項目10 |
| 60 | AVBD_CLI11 | NVARCHAR2(100) | Y | Customized List Items for printing 11 / 顧客専用リスト印字用項目11 |
| 61 | AVBD_CLI12 | NVARCHAR2(100) | Y | Customized List Items for printing 12 / 顧客専用リスト印字用項目12 |
| 62 | AVBD_CLI13 | NVARCHAR2(100) | Y | Customized List Items for printing 13 / 顧客専用リスト印字用項目13 |
| 63 | AVBD_CLI14 | NVARCHAR2(100) | Y | Customized List Items for printing 14 / 顧客専用リスト印字用項目14 |
| 64 | AVBD_CLI15 | NVARCHAR2(100) | Y | Customized List Items for printing 15 / 顧客専用リスト印字用項目15 |
| 65 | AVBD_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 66 | AVBD_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 67 | AVBD_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 68 | AVBD_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 69 | AVBD_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 70 | AVBD_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 71 | AVBD_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 72 | AVBD_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 73 | AVBD_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 74 | AVBD_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 75 | AVBD_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 76 | AVBD_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 77 | AVBD_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 78 | AVBD_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 79 | AVBD_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 80 | AVBD_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 81 | AVBD_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 82 | AVBD_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 83 | AVBD_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 84 | AVBD_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 85 | AVBD_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 86 | AVBD_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 87 | AVBD_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 88 | AVBD_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 89 | AVBD_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 (w/ decimal) / 顧客専用数字項目10 (小数あり) |
| 90 | AVBD_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 (w/ decimal) / 顧客専用数字項目11 (小数あり) |
| 91 | AVBD_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 (w/ decimal) / 顧客専用数字項目12 (小数あり) |
| 92 | AVBD_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 (w/ decimal) / 顧客専用数字項目13 (小数あり) |
| 93 | AVBD_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 (w/ decimal) / 顧客専用数字項目14 (小数あり) |
| 94 | AVBD_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 (w/ decimal) / 顧客専用数字項目15 (小数あり) |
