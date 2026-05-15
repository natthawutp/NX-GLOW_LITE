# GWH_TJ_STK_H
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
| 18 | STKH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | STKH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | STKH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | STKH_STK_NUM | NVARCHAR2(12) | N | Stocktaking Number / 棚卸番号 |
| 22 | STKH_STK_YMD | DATE | N | Stocktaking Date / 棚卸日 |
| 23 | STKH_CNFM_YMD | DATE | Y | Stocktaking Confirm Date / 棚卸完了日 |
| 24 | STKH_STK_STS | CHAR(2) | Y | Stocktaking Status / 棚卸ステータス |
| 25 | STKH_SCDL_QTY | NUMBER(15,3) | Y | Schedule Total Qty / 棚卸トータル予定数 |
| 26 | STKH_RST_QTY | NUMBER(15,3) | Y | Result Total Qty / 棚卸トータル実績数 |
| 27 | STKH_STK_KND | CHAR(2) | N | Stocktaking Kind / 棚卸種類区分 |
| 28 | STKH_STTG_KND | CHAR(2) | N | Stocktaking Target Kind / 棚卸対象区分 |
| 29 | STKH_CKST_KND | CHAR(2) | N | Check Zero Stock Kind / ゼロ在庫チェック区分 |
| 30 | STKH_VLOC_FLG | CHAR(1) | N | Vacant Location Flag / 空ロケーションフラグ |
| 31 | STKH_PRFL_FLG | CHAR(1) | N | Print Finish Label Flag / フィニッシュラベル印刷フラグ |
| 32 | STKH_DQTY_FLG | CHAR(1) | N | Display Result Qty Flag / 棚卸実績数表示フラグ |
| 33 | STKH_STUP_FLG | CHAR(1) | N | Stock Updated Flag / 棚卸在庫更新フラグ |
| 34 | STKH_CLSA_FLG | CHAR(1) | N | Stocktaking Completed Flag (Class A) / クラスＡ棚卸実施フラグ |
| 35 | STKH_CLSB_FLG | CHAR(1) | N | Stocktaking Completed Flag (Class B) / クラスＢ棚卸実施フラグ |
| 36 | STKH_CLSC_FLG | CHAR(1) | N | Stocktaking Completed Flag (Class C) / クラスＣ棚卸実施フラグ |
| 37 | STKH_CRFL_NUM | NUMBER(9) | Y | Current Finish Label Number / Finish Label 発番現在値 |
| 38 | STKH_ABC_WAY | CHAR(2) | Y | ABC Analysis Method / ABC分析方法 |
| 39 | STKH_ABC_DAY | NUMBER(4) | Y | ABC Analysis Term / ABC分析期間 |
| 40 | STKH_AB_LVL | NVARCHAR2(4) | Y | A to B Break point / A-B分岐点 |
| 41 | STKH_BC_LVL | NVARCHAR2(4) | Y | B to C Break point / B-C分岐点 |
| 42 | STKH_FRM_IO_YMD | DATE | Y | In-Out Date (From) / 入出荷日開始 |
| 43 | STKH_TO_IO_YMD | DATE | Y | In-Out Date (To) / 入出荷日終了 |
| 44 | STKH_FRM_ZONE_COD | NVARCHAR2(4) | Y | Zone (From) / ゾーン(From) |
| 45 | STKH_FRM_AREA_COD | CHAR(3) | Y | Area (From) / エリア(From) |
| 46 | STKH_FRM_RACK_COD | NVARCHAR2(20) | Y | Rack (From) / ラック(From) |
| 47 | STKH_FRM_PSTN_COD | NVARCHAR2(6) | Y | Position (From) / ポジション(From) |
| 48 | STKH_FRM_LVL_COD | NVARCHAR2(4) | Y | Level (From) / レベル(From) |
| 49 | STKH_TO_ZONE_COD | NVARCHAR2(4) | Y | Zone (To) / ゾーン(To) |
| 50 | STKH_TO_AREA_COD | CHAR(3) | Y | Area (To) / エリア(To) |
| 51 | STKH_TO_RACK_COD | NVARCHAR2(20) | Y | Rack (To) / ラック(To) |
| 52 | STKH_TO_PSTN_COD | NVARCHAR2(6) | Y | Position (To) / ポジション(To) |
| 53 | STKH_TO_LVL_COD | NVARCHAR2(4) | Y | Level (To) / レベル(To) |
| 54 | STKH_PRDG_COD | NVARCHAR2(30) | Y | Product Group Code / 商品グループコード |
| 55 | STKH_PROD_COD | NVARCHAR2(100) | Y | Product Code / 商品コード |
| 56 | STKH_AV_YMD | DATE | Y | Arrival Date / 入庫日 |
| 57 | STKH_SBIV_COD | NVARCHAR2(40) | Y | Sub Inventry / 等級コード |
| 58 | STKH_DMG_FLG | CHAR(1) | Y | Damage/Hold Flag / ダメージ/ホールドフラグ |
| 59 | STKH_PIK1 | NVARCHAR2(60) | Y | PICKING KEY1 / ピッキングキー1 |
| 60 | STKH_PIK2 | NVARCHAR2(60) | Y | PICKING KEY2 / ピッキングキー2 |
| 61 | STKH_PIK3 | NVARCHAR2(60) | Y | PICKING KEY3 / ピッキングキー3 |
| 62 | STKH_PIK4 | NVARCHAR2(60) | Y | PICKING KEY4 / ピッキングキー4 |
| 63 | STKH_PIK5 | NVARCHAR2(60) | Y | PICKING KEY5 / ピッキングキー5 |
| 64 | STKH_PIK6 | NVARCHAR2(60) | Y | PICKING KEY6 / ピッキングキー6 |
| 65 | STKH_PIK7 | NVARCHAR2(60) | Y | PICKING KEY7 / ピッキングキー7 |
| 66 | STKH_RMKS | NVARCHAR2(1000) | Y | Remarks / 棚卸備考 |
