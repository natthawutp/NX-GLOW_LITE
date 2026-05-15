# GWH_TJ_UJ_R
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
| 18 | UJR_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | UJR_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | UJR_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | UJR_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 入出荷番号 |
| 22 | UJR_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 入出荷ラインNo |
| 23 | UJR_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 入出荷SEQNo |
| 24 | UJR_AS_KND | NVARCHAR2(4) | N | Arrival and Shipping Kind / 入出荷区分 |
| 25 | UJR_TRN_KND | NVARCHAR2(6) | N | Transaction Kind / 伝票区分 |
| 26 | UJR_CRT_YMD | DATE | N | Create Date / レコード作成日 |
| 27 | UJR_AVSP_YMD | DATE | Y | Arrived and Shipped Date / 入出荷日 |
| 28 | UJR_AVSP_STS | NVARCHAR2(6) | N | Arrival and Shipping Status / 入出荷ステータス |
| 29 | UJR_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 30 | UJR_ORGN_COD | CHAR(2) | Y | Country Of Origin / 原産国 |
| 31 | UJR_PPCS_QTY | NUMBER(9) | N | Number Of Pieces Per Case / ケース入数 |
| 32 | UJR_RCS_QTY | NUMBER(9) | N | Case QTY(Result.) / ケース実績数 |
| 33 | UJR_RPC_QTY | NUMBER(12,3) | N | Piece QTY(Result.) / バラ実績数 |
| 34 | UJR_RTPC_QTY | NUMBER(12,3) | N | Total Piece QTY(Result.) / 総バラ実績数 |
| 35 | UJR_AREA_COD | CHAR(3) | Y | Area / エリア |
| 36 | UJR_RACK_COD | NVARCHAR2(20) | Y | Rack / ラック |
| 37 | UJR_PSTN_COD | NVARCHAR2(6) | Y | Position / ポジション |
| 38 | UJR_LVL_COD | NVARCHAR2(4) | Y | Level / レベル |
| 39 | UJR_SBIV_COD | NVARCHAR2(40) | N | Sub Inventry / 等級コード |
| 40 | UJR_PIK1 | NVARCHAR2(60) | Y | PICKING KEY1 / ピッキングキー1 |
| 41 | UJR_PIK2 | NVARCHAR2(60) | Y | PICKING KEY2 / ピッキングキー2 |
| 42 | UJR_PIK3 | NVARCHAR2(60) | Y | PICKING KEY3 / ピッキングキー3 |
| 43 | UJR_PIK4 | NVARCHAR2(60) | Y | PICKING KEY4 / ピッキングキー4 |
| 44 | UJR_PIK5 | NVARCHAR2(60) | Y | PICKING KEY5 / ピッキングキー5 |
| 45 | UJR_PIK6 | NVARCHAR2(60) | Y | PICKING KEY6 / ピッキングキー6 |
| 46 | UJR_PIK7 | NVARCHAR2(60) | Y | PICKING KEY7 / ピッキングキー7 |
| 47 | UJR_AV_NUM | NVARCHAR2(12) | Y | Arrival Number / 入荷番号 |
| 48 | UJR_AVLN_NUM | NUMBER(4) | Y | Arrival Line No / 入荷ラインNo |
| 49 | UJR_AVSQ_NUM | NUMBER(4) | Y | Arrival Seq No / 入荷SEQNo |
| 50 | UJR_DMG_FLG | CHAR(1) | N | Damage/Hold Flag / ダメージ/ホールドフラグ |
| 51 | UJR_OP_FLG | CHAR(1) | N | Operation Flag / 作業フラグ(入荷：棚付、出荷：ピッキング) |
| 52 | UJR_CNFM_FLG | CHAR(1) | N | Confirmed Flag / 確定フラグ |
| 53 | UJR_RSN_COD | NVARCHAR2(6) | Y | Reason Code / 理由コード |
| 54 | UJR_RMKS | NVARCHAR2(1000) | Y | Remarks / 備考 |
| 55 | UJR_PPB_QTY | NUMBER(9) | Y | Number Of Pieces Per Ball / ボール入数 |
| 56 | UJR_RBL_QTY | NUMBER(9) | N | Ball QTY(Result.) / ボール実績数 |
