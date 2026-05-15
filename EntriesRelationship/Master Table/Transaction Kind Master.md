# GWH_TM_TRN
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
| 18 | TRN_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | TRN_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | TRN_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | TRN_KND | NVARCHAR2(6) | N | Transaction Kind / 伝票区分 |
| 22 | TRN_NAM | NVARCHAR2(100) | Y | Transaction Name / 伝票区分名称 |
| 23 | TRN_AVCC_FLG | CHAR(1) | N | Arrival Charge calculation Flag / 入庫倉庫料金計算フラグ |
| 24 | TRN_SPCC_FLG | CHAR(1) | N | Shipping Charge calculation Flag / 出庫倉庫料金計算フラグ |
| 25 | TRN_STCC_FLG | CHAR(1) | N | Storage Charge calculation Flag / 保管倉庫料金計算フラグ |
| 26 | TRN_DSCC_FLG | CHAR(1) | N | Kitting Charge calculation Flag / 流通加工料金計算フラグ |
| 27 | TRN_RSNR_FLG | CHAR(1) | N | Reason Code Required Flag / 理由コード必須フラグ |
| 28 | TRN_PKDP_FLG | CHAR(1) | N | Pick Key Discrepancy Permission at Arrival Flag / 入荷検品ピックキー差異許可フラグ |
| 29 | TRN_SPPA_FLG | CHAR(1) | N | Surplus Permission at Arrival Flag / 入荷余剰許可フラグ |
| 30 | TRN_SHPA_FLG | CHAR(1) | N | Shortage Permission at Arrival Flag / 入荷不足許可フラグ |
| 31 | TRN_AVUS_FLG | CHAR(1) | N | Arrival usage Flag / 入荷使用フラグ |
| 32 | TRN_UIUS_FLG | CHAR(1) | N | Unplan In usage Flag / 入庫使用フラグ |
| 33 | TRN_SPUS_FLG | CHAR(1) | N | Shipping usage Flag / 出荷使用フラグ |
| 34 | TRN_UOUS_FLG | CHAR(1) | N | Unplan Out usage Flag / 出庫使用フラグ |
| 35 | TRN_STAJ_FLG | CHAR(1) | N | Stock Adjustment usage Flag / 在庫調整使用フラグ |
| 36 | TRN_RLUS_FLG | CHAR(1) | N | Re-location usage Flag / 棚振使用フラグ |
| 37 | TRN_PTUS_FLG | CHAR(1) | N | Product Transfer usage Flag / 商振使用フラグ |
| 38 | TRN_KAUS_FLG | CHAR(1) | N | Kitting Arrival usage Flag / 組立入庫使用フラグ |
| 39 | TRN_KSUS_FLG | CHAR(1) | N | Kitting Shipping usage Flag / 組立出庫使用フラグ |
| 40 | TRN_BAUS_FLG | CHAR(1) | N | Break Arrival usage Flag / 解体入庫使用フラグ |
| 41 | TRN_BSUS_FLG | CHAR(1) | N | Break Shipping usage Flag / 解体出庫使用フラグ |
| 42 | TRN_KPIS_FLG | CHAR(1) | N | KPI Shipping Frequency Sum Target Flag / KPI出荷頻度集計対象フラグ |
| 43 | TRN_BNDC_FLG | CHAR(1) | N | Bond Storage List Incoming and Outgoing Kind / 保税残高表入出庫フラグ |
| 44 | TRN_PDAC_FLG | CHAR(1) | N | Partial Data Auto Creating Flag / 分納データ自動作成フラグ |
| 45 | TRN_DVUS_FLG | CHAR(1) | N | Divide usage Flag / 分割使用フラグ |
| 46 | TRN_OSOP_KND | CHAR(2) | N | Out of stock type kind/欠品時処理区分 |
| 47 | TRN_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 48 | TRN_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 49 | TRN_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 50 | TRN_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 51 | TRN_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 52 | TRN_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 53 | TRN_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 54 | TRN_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 55 | TRN_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 56 | TRN_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 57 | TRN_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 58 | TRN_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 59 | TRN_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 60 | TRN_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 61 | TRN_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 62 | TRN_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 63 | TRN_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 64 | TRN_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 65 | TRN_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 66 | TRN_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 67 | TRN_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 68 | TRN_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 69 | TRN_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 70 | TRN_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 71 | TRN_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 72 | TRN_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 73 | TRN_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 74 | TRN_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 75 | TRN_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 76 | TRN_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 77 | TRN_CIT1 | NVARCHAR2(200) | Y | Customized Item 01 / 顧客専用項目01 |
| 78 | TRN_CIT2 | NVARCHAR2(200) | Y | Customized Item 02 / 顧客専用項目02 |
| 79 | TRN_CIT3 | NVARCHAR2(200) | Y | Customized Item 03 / 顧客専用項目03 |
| 80 | TRN_CIT4 | NVARCHAR2(200) | Y | Customized Item 04 / 顧客専用項目04 |
| 81 | TRN_CIT5 | NVARCHAR2(200) | Y | Customized Item 05 / 顧客専用項目05 |
| 82 | TRN_CIT6 | NVARCHAR2(200) | Y | Customized Item 06 / 顧客専用項目06 |
| 83 | TRN_CIT7 | NVARCHAR2(200) | Y | Customized Item 07 / 顧客専用項目07 |
| 84 | TRN_CIT8 | NVARCHAR2(200) | Y | Customized Item 08 / 顧客専用項目08 |
| 85 | TRN_CIT9 | NVARCHAR2(200) | Y | Customized Item 09 / 顧客専用項目09 |
| 86 | TRN_CIT10 | NVARCHAR2(200) | Y | Customized Item 10 / 顧客専用項目10 |
| 87 | TRN_CIT11 | NVARCHAR2(200) | Y | Customized Item 11 / 顧客専用項目11 |
| 88 | TRN_CIT12 | NVARCHAR2(200) | Y | Customized Item 12 / 顧客専用項目12 |
| 89 | TRN_CIT13 | NVARCHAR2(200) | Y | Customized Item 13 / 顧客専用項目13 |
| 90 | TRN_CIT14 | NVARCHAR2(200) | Y | Customized Item 14 / 顧客専用項目14 |
| 91 | TRN_CIT15 | NVARCHAR2(200) | Y | Customized Item 15 / 顧客専用項目15 |
