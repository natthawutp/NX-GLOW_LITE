# GWH_TJ_SRL
#Entity #Standard #INVENTORY

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
| 18 | SRL_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SRL_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SRL_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SRL_AV_NUM | NVARCHAR2(12) | N | Arrival Number / 入荷番号 |
| 22 | SRL_AVLN_NUM | NUMBER(4) | N | Arrival Line No / 入荷ラインNo |
| 23 | SRL_AVSQ_NUM | NUMBER(4) | N | Arrival Seq No / 入荷SEQNo |
| 24 | SRL_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 25 | SRL_ORGN_COD | CHAR(2) | Y | Country Of Origin / 原産国 |
| 26 | SRL_SRL_NUM | NVARCHAR2(100) | N | Serial number / シリアル番号 |
| 27 | SRL_GET_KND | CHAR(1) | N | Serial Get Kind / シリアル取得区分 |
| 28 | SRL_SRST_COD | CHAR(1) | N | Serial Status / シリアルステータス |
| 29 | SRL_RPC_QTY | NUMBER(12,3) | N | Piece QTY(Result.) / バラ実績数 |
| 30 | SRL_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 入出荷番号 |
| 31 | SRL_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 入出荷ラインNo |
| 32 | SRL_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 入出荷SEQNo |
| 33 | SRL_AS_KND | NVARCHAR2(4) | N | Arrival and Shipping Kind / 入出荷区分 |
| 34 | SRL_TRN_KND | NVARCHAR2(6) | N | Transaction Kind / 伝票区分 |
| 35 | SRL_TRN_YMD | DATE | N | Transaction Date / 処理日 |
| 36 | SRL_PCCT_NUM | NVARCHAR2(40) | Y | Packing Carton ID / カートンID |
| 37 | SRL_EDI_FLG | CHAR(1) | N | EDI Flag / EDI登録フラグ |
| 38 | SRL_RCV_BTCH_NUM | NVARCHAR2(60) | Y | Recive Batch Number / 受信バッチNo |
| 39 | SRL_RCV_BTCH_YMD | DATE | Y | Recive Batch Date / 受信バッチ日 |
| 40 | SRL_SND_BTCH_NUM | NVARCHAR2(60) | Y | Send Batch Number / 送信バッチNo |
| 41 | SRL_SND_BTCH_YMD | DATE | Y | Send Batch Date / 送信バッチ日 |
| 42 | SRL_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 43 | SRL_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 44 | SRL_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 45 | SRL_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 46 | SRL_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 47 | SRL_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 48 | SRL_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 49 | SRL_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 50 | SRL_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 51 | SRL_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 52 | SRL_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 53 | SRL_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 54 | SRL_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 55 | SRL_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 56 | SRL_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 57 | SRL_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 58 | SRL_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 59 | SRL_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 60 | SRL_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 61 | SRL_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 62 | SRL_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 63 | SRL_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 64 | SRL_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 65 | SRL_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 66 | SRL_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 67 | SRL_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 68 | SRL_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 69 | SRL_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 70 | SRL_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 71 | SRL_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 72 | SRL_CIT1 | NVARCHAR2(200) | Y | Customized Item 01 / 顧客専用項目01 |
| 73 | SRL_CIT2 | NVARCHAR2(200) | Y | Customized Item 02 / 顧客専用項目02 |
| 74 | SRL_CIT3 | NVARCHAR2(200) | Y | Customized Item 03 / 顧客専用項目03 |
| 75 | SRL_CIT4 | NVARCHAR2(200) | Y | Customized Item 04 / 顧客専用項目04 |
| 76 | SRL_CIT5 | NVARCHAR2(200) | Y | Customized Item 05 / 顧客専用項目05 |
| 77 | SRL_CIT6 | NVARCHAR2(200) | Y | Customized Item 06 / 顧客専用項目06 |
| 78 | SRL_CIT7 | NVARCHAR2(200) | Y | Customized Item 07 / 顧客専用項目07 |
| 79 | SRL_CIT8 | NVARCHAR2(200) | Y | Customized Item 08 / 顧客専用項目08 |
| 80 | SRL_CIT9 | NVARCHAR2(200) | Y | Customized Item 09 / 顧客専用項目09 |
| 81 | SRL_CIT10 | NVARCHAR2(200) | Y | Customized Item 10 / 顧客専用項目10 |
| 82 | SRL_CIT11 | NVARCHAR2(200) | Y | Customized Item 11 / 顧客専用項目11 |
| 83 | SRL_CIT12 | NVARCHAR2(200) | Y | Customized Item 12 / 顧客専用項目12 |
| 84 | SRL_CIT13 | NVARCHAR2(200) | Y | Customized Item 13 / 顧客専用項目13 |
| 85 | SRL_CIT14 | NVARCHAR2(200) | Y | Customized Item 14 / 顧客専用項目14 |
| 86 | SRL_CIT15 | NVARCHAR2(200) | Y | Customized Item 15 / 顧客専用項目15 |
