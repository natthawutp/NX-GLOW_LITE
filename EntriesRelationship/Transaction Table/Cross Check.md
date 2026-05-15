# GWH_TJ_XC
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
| 18 | XC_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | XC_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | XC_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | XC_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 入出荷番号 |
| 22 | XC_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 入出荷ラインNo |
| 23 | XC_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 入出荷SEQNo |
| 24 | XC_CATM_NUM | NUMBER(3) | N | Canceled times / キャンセル回数 |
| 25 | XC_AS_KND | NVARCHAR2(4) | N | Arrival and Shipping Kind / 入出荷区分 |
| 26 | XC_TRN_KND | NVARCHAR2(6) | N | Transaction Kind / 伝票区分 |
| 27 | XC_CRT_YMD | DATE | N | Create Date / レコード作成日 |
| 28 | XC_AVSP_YMD | DATE | Y | Arrived and Shipped Date / 入出荷日 |
| 29 | XC_AVSP_STS | NVARCHAR2(6) | N | Arrival and Shipping Status / 入出荷ステータス |
| 30 | XC_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 31 | XC_ORGN_COD | CHAR(2) | Y | Country Of Origin / 原産国 |
| 32 | XC_PPCS_QTY | NUMBER(9) | N | Number Of Pieces Per Case / ケース入数 |
| 33 | XC_RCS_QTY | NUMBER(9) | N | Case QTY(Result.) / ケース実績数 |
| 34 | XC_RPC_QTY | NUMBER(12,3) | N | Piece QTY(Result.) / バラ実績数 |
| 35 | XC_RTPC_QTY | NUMBER(12,3) | N | Total Piece QTY(Result.) / 総バラ実績数 |
| 36 | XC_AREA_COD | CHAR(3) | Y | Area / エリア |
| 37 | XC_RACK_COD | NVARCHAR2(20) | Y | Rack / ラック |
| 38 | XC_PSTN_COD | NVARCHAR2(6) | Y | Position / ポジション |
| 39 | XC_LVL_COD | NVARCHAR2(4) | Y | Level / レベル |
| 40 | XC_SBIV_COD | NVARCHAR2(40) | N | Sub Inventry / 等級コード |
| 41 | XC_PIK1 | NVARCHAR2(60) | Y | PICKING KEY1 / ピッキングキー1 |
| 42 | XC_PIK2 | NVARCHAR2(60) | Y | PICKING KEY2 / ピッキングキー2 |
| 43 | XC_PIK3 | NVARCHAR2(60) | Y | PICKING KEY3 / ピッキングキー3 |
| 44 | XC_PIK4 | NVARCHAR2(60) | Y | PICKING KEY4 / ピッキングキー4 |
| 45 | XC_PIK5 | NVARCHAR2(60) | Y | PICKING KEY5 / ピッキングキー5 |
| 46 | XC_PIK6 | NVARCHAR2(60) | Y | PICKING KEY6 / ピッキングキー6 |
| 47 | XC_PIK7 | NVARCHAR2(60) | Y | PICKING KEY7 / ピッキングキー7 |
| 48 | XC_AV_NUM | NVARCHAR2(12) | Y | Arrival Number / 入荷番号 |
| 49 | XC_AVLN_NUM | NUMBER(4) | Y | Arrival Line No / 入荷ラインNo |
| 50 | XC_AVSQ_NUM | NUMBER(4) | Y | Arrival Seq No / 入荷SEQNo |
| 51 | XC_DMG_FLG | CHAR(1) | N | Damage/Hold Flag / ダメージ/ホールドフラグ |
| 52 | XC_OP_FLG | CHAR(1) | N | Operation Flag / 作業フラグ(入荷：棚付、出荷：ピッキング) |
| 53 | XC_CNFM_FLG | CHAR(1) | N | Confirmed Flag / 確定フラグ |
| 54 | XC_RSN_COD | NVARCHAR2(6) | Y | Reason Code / 理由コード |
| 55 | XC_RMKS | NVARCHAR2(1000) | Y | Remarks / 備考 |
| 56 | XC_RWGT | NUMBER(12,6) | Y | Weight(Result.) / 実績重量 |
| 57 | XC_RM3 | NUMBER(12,6) | Y | Volume(Result.) / 実績容積 |
| 58 | XC_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 59 | XC_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 60 | XC_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 61 | XC_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 62 | XC_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 63 | XC_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 64 | XC_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 65 | XC_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 66 | XC_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 67 | XC_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 68 | XC_COI11 | NVARCHAR2(200) | Y | Customized Only Item 11 / カスタマイズ項目11 |
| 69 | XC_COI12 | NVARCHAR2(200) | Y | Customized Only Item 12 / カスタマイズ項目12 |
| 70 | XC_COI13 | NVARCHAR2(200) | Y | Customized Only Item 13 / カスタマイズ項目13 |
| 71 | XC_COI14 | NVARCHAR2(200) | Y | Customized Only Item 14 / カスタマイズ項目14 |
| 72 | XC_COI15 | NVARCHAR2(200) | Y | Customized Only Item 15 / カスタマイズ項目15 |
| 73 | XC_COI16 | NVARCHAR2(200) | Y | Customized Only Item 16 / カスタマイズ項目16 |
| 74 | XC_COI17 | NVARCHAR2(200) | Y | Customized Only Item 17 / カスタマイズ項目17 |
| 75 | XC_COI18 | NVARCHAR2(200) | Y | Customized Only Item 18 / カスタマイズ項目18 |
| 76 | XC_COI19 | NVARCHAR2(200) | Y | Customized Only Item 19 / カスタマイズ項目19 |
| 77 | XC_COI20 | NVARCHAR2(200) | Y | Customized Only Item 20 / カスタマイズ項目20 |
| 78 | XC_COI21 | NVARCHAR2(200) | Y | Customized Only Item 21 / カスタマイズ項目21 |
| 79 | XC_COI22 | NVARCHAR2(200) | Y | Customized Only Item 22 / カスタマイズ項目22 |
| 80 | XC_COI23 | NVARCHAR2(200) | Y | Customized Only Item 23 / カスタマイズ項目23 |
| 81 | XC_COI24 | NVARCHAR2(200) | Y | Customized Only Item 24 / カスタマイズ項目24 |
| 82 | XC_COI25 | NVARCHAR2(200) | Y | Customized Only Item 25 / カスタマイズ項目25 |
| 83 | XC_COI26 | NVARCHAR2(200) | Y | Customized Only Item 26 / カスタマイズ項目26 |
| 84 | XC_COI27 | NVARCHAR2(200) | Y | Customized Only Item 27 / カスタマイズ項目27 |
| 85 | XC_COI28 | NVARCHAR2(200) | Y | Customized Only Item 28 / カスタマイズ項目28 |
| 86 | XC_COI29 | NVARCHAR2(200) | Y | Customized Only Item 29 / カスタマイズ項目29 |
| 87 | XC_COI30 | NVARCHAR2(200) | Y | Customized Only Item 30 / カスタマイズ項目30 |
| 88 | XC_COI31 | NVARCHAR2(200) | Y | Customized Only Item 31 / カスタマイズ項目31 |
| 89 | XC_COI32 | NVARCHAR2(200) | Y | Customized Only Item 32 / カスタマイズ項目32 |
| 90 | XC_COI33 | NVARCHAR2(200) | Y | Customized Only Item 33 / カスタマイズ項目33 |
| 91 | XC_COI34 | NVARCHAR2(200) | Y | Customized Only Item 34 / カスタマイズ項目34 |
| 92 | XC_COI35 | NVARCHAR2(200) | Y | Customized Only Item 35 / カスタマイズ項目35 |
| 93 | XC_COI36 | NVARCHAR2(200) | Y | Customized Only Item 36 / カスタマイズ項目36 |
| 94 | XC_COI37 | NVARCHAR2(200) | Y | Customized Only Item 37 / カスタマイズ項目37 |
| 95 | XC_COI38 | NVARCHAR2(200) | Y | Customized Only Item 38 / カスタマイズ項目38 |
| 96 | XC_COI39 | NVARCHAR2(200) | Y | Customized Only Item 39 / カスタマイズ項目39 |
| 97 | XC_COI40 | NVARCHAR2(200) | Y | Customized Only Item 40 / カスタマイズ項目40 |
| 98 | XC_COIL1 | NVARCHAR2(510) | Y | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| 99 | XC_COIL2 | NVARCHAR2(510) | Y | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| 100 | XC_COIL3 | NVARCHAR2(510) | Y | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| 101 | XC_COIL4 | NVARCHAR2(510) | Y | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| 102 | XC_COIL5 | NVARCHAR2(510) | Y | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
| 103 | XC_COIL6 | NVARCHAR2(510) | Y | Customized Only Item (Long) 06 / カスタマイズ項目(long)06 |
| 104 | XC_COIL7 | NVARCHAR2(510) | Y | Customized Only Item (Long) 07 / カスタマイズ項目(long)07 |
| 105 | XC_COIL8 | NVARCHAR2(510) | Y | Customized Only Item (Long) 08 / カスタマイズ項目(long)08 |
| 106 | XC_COIL9 | NVARCHAR2(510) | Y | Customized Only Item (Long) 09 / カスタマイズ項目(long)09 |
| 107 | XC_COIL10 | NVARCHAR2(510) | Y | Customized Only Item (Long) 10 / カスタマイズ項目(long)10 |
| 108 | XC_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 109 | XC_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 110 | XC_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 111 | XC_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 112 | XC_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 113 | XC_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 114 | XC_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 115 | XC_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 116 | XC_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 117 | XC_CNI10_NUM | NUMBER(9) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 118 | XC_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 119 | XC_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 120 | XC_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 121 | XC_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 122 | XC_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 123 | XC_PPB_QTY | NUMBER(9) | Y | Number Of Pieces Per Ball / ボール入数 |
| 124 | XC_RBL_QTY | NUMBER(9) | N | Ball QTY(Result.) / ボール実績数 |
