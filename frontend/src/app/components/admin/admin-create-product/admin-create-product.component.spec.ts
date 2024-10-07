import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCreateProductComponent } from './admin-create-product.component';

describe('AdminCreateProductComponent', () => {
  let component: AdminCreateProductComponent;
  let fixture: ComponentFixture<AdminCreateProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCreateProductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCreateProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
