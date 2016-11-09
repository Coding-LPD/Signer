import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

declare var BMap: any;

@Component({
  selector: 'baidu-map',
  template: '<div id="my-map" class="map"></div>',
  styles: [`
    .map {
      width: 100%;
      height: 100%;
      overflow: hidden;
      margin: 0;      
    }
  `]
})
export class BaiduMapComponent implements OnInit {
  
  private _map: any = null;
  private _lng: number = this._defaultLng;
  private _lat: number = this._defaultLat;
  private _defaultLng = 116.404355;
  private _defaultLat = 39.915219; 

  @Input() set lng(val: number) {
    this._lng = val || this._defaultLng;
    this.locate();
  }

  @Input() set lat(val: number) {
    this._lat = val || this._defaultLat;
    this.locate();
  }

  @Input() zoom: number = 17;
  @Output() mapClick = new EventEmitter<any>();  

  get map() {
    if (!this._map) {      
      this._map = new BMap.Map('my-map');
    }
    return this._map;
  }

  ngOnInit() {    
    this.map.enableScrollWheelZoom(true);

    // 点击地图获取坐标
    this.map.addEventListener("click", (e: any) => {
      this._lng = e.point.lng;
      this._lat = e.point.lat;
      this.locate(false);      
      this.mapClick.emit({
        lng: e.point.lng,
        lat: e.point.lat 
      });
    });    
  }
  
  locate(toCenter: boolean = true) {
    var point = new BMap.Point(this._lng, this._lat);
    var marker = new BMap.Marker(point);    
    if (toCenter) {
      this.map.centerAndZoom(point, this.zoom);
    }
    this.map.clearOverlays();
    this.map.addOverlay(marker);
  }

}