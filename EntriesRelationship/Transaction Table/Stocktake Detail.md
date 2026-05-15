# GWH_TJ_STK_D
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
| 18 | STKD_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | STKD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | STKD_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | STKD_STK_NUM | NVARCHAR2(12) | N | Stocktaking Number / 棚卸番号 |
| 22 | STKD_RSTE_KND | CHAR(2) | N | Stocktaking Result Entry Kind / 棚卸結果入力区分 |
| 23 | STKD_CNT_NUM | NUMBER(9) | N | Number of count / カウント回数 |
| 24 | STKD_DATA_KND | CHAR(2) | Y | Stocktaking Data Creation Kind / 棚卸データ作成区分 |
| 25 | STKD_AV_NUM | NVARCHAR2(12) | N | Arrival Number / 入荷番号 |
| 26 | STKD_AVLN_NUM | NUMBER(4) | N | Arrival Line No / 入荷ラインNo |
| 27 | STKD_AVSQ_NUM | NUMBER(4) | N | Arrival Seq No / 入荷SEQNo |
| 28 | STKD_ST_KND | CHAR(1) | N | Stock Control Kind / 在庫管理区分 |
| 29 | STKD_AV_YMD | DATE | N | Arrival Date / 入庫日 |
| 30 | STKD_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 31 | STKD_ORGN_COD | CHAR(2) | Y | Country Of Origin / 原産国 |
| 32 | STKD_PIK1 | NVARCHAR2(60) | Y | PICKING KEY1 / ピッキングキー1 |
| 33 | STKD_PIK2 | NVARCHAR2(60) | Y | PICKING KEY2 / ピッキングキー2 |
| 34 | STKD_PIK3 | NVARCHAR2(60) | Y | PICKING KEY3 / ピッキングキー3 |
| 35 | STKD_PIK4 | NVARCHAR2(60) | Y | PICKING KEY4 / ピッキングキー4 |
| 36 | STKD_PIK5 | NVARCHAR2(60) | Y | PICKING KEY5 / ピッキングキー5 |
| 37 | STKD_PIK6 | NVARCHAR2(60) | Y | PICKING KEY6 / ピッキングキー6 |
| 38 | STKD_PIK7 | NVARCHAR2(60) | Y | PICKING KEY7 / ピッキングキー7 |
| 39 | STKD_ZONE_COD | NVARCHAR2(4) | Y | Zone Code / ゾーン |
| 40 | STKD_AREA_COD | CHAR(3) | N | Area / エリア |
| 41 | STKD_RACK_COD | NVARCHAR2(20) | Y | Rack / ラック |
| 42 | STKD_PSTN_COD | NVARCHAR2(6) | Y | Position / ポジション |
| 43 | STKD_LVL_COD | NVARCHAR2(4) | Y | Level / レベル |
| 44 | STKD_SBIV_COD | NVARCHAR2(40) | N | Sub Inventry / 等級コード |
| 45 | STKD_DMG_FLG | CHAR(1) | Y | Damage/Hold Flag / ダメージ/ホールドフラグ |
| 46 | STKD_BOND_FLG | CHAR(1) | Y | Bond Flag / 保税フラグ |
| 47 | STKD_PPCS_QTY | NUMBER(9) | Y | Number Of Pieces Per Case / ケース入数 |
| 48 | STKD_SCS_QTY | NUMBER(9) | Y | Case QTY (Stock) / 在庫ケース数量 |
| 49 | STKD_SPC_QTY | NUMBER(12,3) | Y | Piece QTY (Stock) / 在庫バラ数量 |
| 50 | STKD_STPC_QTY | NUMBER(12,3) | Y | Total Piece QTY (Stock) / 在庫総バラ数量 |
| 51 | STKD_RCS_QTY | NUMBER(9) | Y | Case QTY(Result.) / 棚卸実績ケース数量 |
| 52 | STKD_RPC_QTY | NUMBER(12,3) | Y | Piece QTY(Result.) / 棚卸実績バラ数量 |
| 53 | STKD_RTPC_QTY | NUMBER(12,3) | Y | Total Piece QTY(Result.) / 棚卸実績総バラ数量 |
| 54 | STKD_DCS_QTY | NUMBER(9) | Y | Case QTY (Disclapency) / 棚卸差異ケース数量 |
| 55 | STKD_DPC_QTY | NUMBER(12,3) | Y | Piece QTY (Disclapency) / 棚卸差異バラ数量 |
| 56 | STKD_DTPC_QTY | NUMBER(12,3) | Y | Total Piece QTY (Disclapency) / 棚卸差異総バラ数量 |
| 57 | STKD_RMKS | NVARCHAR2(1000) | Y | Remarks / 棚卸備考 |
| 58 | STKD_PPB_QTY | NUMBER(9) | Y | Number Of Pieces Per Ball / ボール入数 |
| 59 | STKD_SBL_QTY | NUMBER(9) | Y | Ball QTY(Stock) / 在庫ボール数 |
| 60 | STKD_RBL_QTY | NUMBER(9) | Y | Ball QTY(Result.) / 棚卸実績ボール数 |
| 61 | STKD_DBL_QTY | NUMBER(9) | Y | Ball QTY(Disclapency) / 棚卸差異ボール数 |
